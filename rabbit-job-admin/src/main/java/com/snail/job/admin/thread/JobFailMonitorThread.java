package com.snail.job.admin.thread;

import com.snail.job.admin.alarm.JobAlarm;
import com.snail.job.admin.model.JobInfo;
import com.snail.job.admin.model.JobLog;
import com.snail.job.admin.service.IJobInfoService;
import com.snail.job.admin.service.IJobLogService;
import com.snail.job.admin.service.trigger.TriggerPoolService;
import com.snail.job.common.thread.RabbitJobAbstractThread;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static com.snail.job.admin.constant.AdminConstants.FAIL_MONITOR_INTERVAL;
import static com.snail.job.common.enums.AlarmStatus.ALARM_FINISH;
import static com.snail.job.common.enums.TriggerType.RETRY;

/**
 * 扫描调度失败的任务，进行重新调度或报警
 * 每秒钟执行一次
 *
 * @author 吴庆龙
 * @date 2020/7/20 10:40 上午
 */
@Component
public class JobFailMonitorThread extends RabbitJobAbstractThread {

    @Resource
    private TriggerPoolService triggerPoolService;

    @Resource
    private IJobLogService jobLogService;
    @Resource
    private IJobInfoService jobInfoService;


    /**
     * 报警方式集合
     */
    @Resource
    private List<JobAlarm> jobAlarmList;

    @Override
    protected void doRun() throws InterruptedException {
        long startMillis = System.currentTimeMillis();

        /*
            哪些任务是失败的呢？
            1. 调度失败：trigger_code=0 || trigger_code!=200
            2. 任务执行失败：exec_code=0 || exec_code!=200
            3. alarm_status=0
         */

        // 查询所有需要告警的任务日志
        List<JobLog> failJobLogList = jobLogService.findAllFailJobLog();
        for (JobLog jobLog : failJobLogList) {
            JobInfo jobInfo = jobInfoService.getById(jobLog.getJobId());
            if (jobInfo == null) {
                // 将任务设置为失败
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
        List<JobLog> updateJobLogList = new ArrayList<>(failJobLogList.size());
        for (JobLog jobLog : failJobLogList) {
            JobLog updateJobLog = new JobLog()
                    .setId(jobLog.getId())
                    .setAlarmStatus(ALARM_FINISH.getValue());
            updateJobLogList.add(updateJobLog);
        }
        // TODO 测试是否会更新其它字段
        jobLogService.updateBatchById(updateJobLogList);

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
