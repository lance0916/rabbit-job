package com.snail.job.admin.thread;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.snail.job.admin.alarm.JobAlarm;
import com.snail.job.admin.model.JobInfo;
import com.snail.job.admin.model.JobLog;
import com.snail.job.admin.service.JobInfoService;
import com.snail.job.admin.service.JobLogService;
import com.snail.job.admin.service.trigger.TriggerPoolService;
import com.snail.job.common.thread.RabbitJobAbstractThread;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;
import static com.snail.job.admin.constant.AdminConstants.FAIL_MONITOR_INTERVAL;
import static com.snail.job.common.enums.AlarmStatus.ALARM_FINISH;
import static com.snail.job.common.enums.TriggerType.RETRY;

/**
 * 扫描调度失败的任务，进行重新调度或报警
 * 每秒钟执行一次
 * @author WuQinglong
 */
@Component
public class JobFailMonitorThread extends RabbitJobAbstractThread {

    @Resource
    private TriggerPoolService triggerPoolService;

    @Resource
    private JobLogService jobLogService;
    @Resource
    private JobInfoService jobInfoService;

    /**
     * 报警方式集合
     */
    @Resource
    private List<JobAlarm> jobAlarmList;

    @Override
    protected void execute() throws InterruptedException {
        long startMillis = System.currentTimeMillis();

        /*
            哪些任务是失败的呢？
            1. 调度失败：trigger_code=0 || trigger_code!=200
            2. 任务执行失败：exec_code=0 || exec_code!=200
            3. alarm_status=0
         */

        // 查询所有需要告警的任务日志
        List<JobLog> failJobLogs = jobLogService.list(Wrappers.<JobLog>query()
                .select(JobLog.ID, JobLog.JOB_ID, JobLog.FAIL_RETRY_COUNT)
                .eq(JobLog.ALARM_STATUS, 1)
                .notIn(JobLog.TRIGGER_CODE, 0, 200)
                .notIn(JobLog.EXEC_CODE, 0, 200));
        for (JobLog jobLog : failJobLogs) {
            JobInfo jobInfo = jobInfoService.getById(jobLog.getJobId());
            if (jobInfo == null) {
                log.error("任务日志中的jobId没有对应的记录。jobLogId:{}", jobLog.getId());
                continue;
            }

            // 重新调度
            Integer failRetryCount = jobLog.getFailRetryCount();
            if (failRetryCount > 0) {
                // 下次任务的重试次数
                int finalFailRetryCount = failRetryCount - 1;

                // 进行任务调度，会增加一条调度日志
                triggerPoolService.trigger(jobLog.getJobId(), finalFailRetryCount, jobLog.getExecParam(), RETRY);
            }

            // 告警
            for (JobAlarm jobAlarm : jobAlarmList) {
                jobAlarm.doAlarm(jobInfo, jobLog);
            }
        }

        // 批量更新告警状态
        for (JobLog jobLog : failJobLogs) {
            jobLog.setAlarmStatus(ALARM_FINISH.getValue());
        }
        jobLogService.updateBatchById(failJobLogs);

        // 休眠
        long costMillis = System.currentTimeMillis() - startMillis;
        if (running && costMillis < FAIL_MONITOR_INTERVAL) {
            Thread.sleep(FAIL_MONITOR_INTERVAL - costMillis);
        }
    }

    @Override
    protected String getThreadName() {
        return "FailMonitorThread";
    }
}
