package com.snail.job.admin.thread;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.snail.job.admin.model.JobInfo;
import com.snail.job.admin.service.JobInfoService;
import com.snail.job.admin.service.trigger.TriggerPoolService;
import com.snail.job.common.thread.RabbitJobAbstractThread;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.snail.job.admin.constant.AdminConstants.JOB_PRE_SCAN_TIME;
import static com.snail.job.admin.constant.AdminConstants.JOB_SCHEDULE_INTERVAL;
import static com.snail.job.common.enums.TriggerStatus.RUNNING;
import static com.snail.job.common.enums.TriggerStatus.STOPPED;
import static com.snail.job.common.enums.TriggerType.CRON;

/**
 * 定时任务调度类
 * @author 吴庆龙
 */
@Component
public class JobScheduleThread extends RabbitJobAbstractThread {

    @Resource
    private TriggerPoolService triggerPoolService;
    @Resource
    private JobInfoService jobInfoService;

    @Override
    protected void doRun() throws InterruptedException {
        long startMillis = System.currentTimeMillis();

        // 当前时间
        LocalDateTime curTime = LocalDateTime.now();
        LocalDateTime endTime = curTime.plusSeconds(JOB_PRE_SCAN_TIME);

        // 扫描将要待执行的任务，根据任务的执行时间从近到远排序
        QueryWrapper<JobInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("trigger_status", RUNNING.getValue())
                .and(wrapper -> wrapper.lt("trigger_next_time", endTime).or().isNull("trigger_next_time"))
                .orderByDesc("trigger_next_time");
        List<JobInfo> waitTriggerJobs = jobInfoService.list(queryWrapper);

        // 遍历待执行的任务
        for (JobInfo info : waitTriggerJobs) {
            // 任务的下次执行时间
            LocalDateTime nextTime = info.getTriggerNextTime();
            // 如果没有执行过，设置为当前时间
            if (nextTime == null) {
                nextTime = curTime;
            }

            // 1、过时的任务，忽略调度并更新下次的执行时间
            if (nextTime.isBefore(curTime)) {
                // 刷新下次调度时间
                refreshNextValidTime(info, LocalDateTime.now());
            }
            // 2、当前秒要执行的任务, 直接进行调度
            else if (nextTime.isEqual(curTime)) {
                // 直接进行调度
                triggerPoolService.trigger(info.getId(), -1, null, CRON);

                // 刷新下次调度时间
                refreshNextValidTime(info, nextTime);
            }

            // 重新获取任务的下次执行时间
            nextTime = info.getTriggerNextTime();

            // 3、在 [当前秒+1秒，当前时间 + 10秒) 时间内要调度？
            while (nextTime != null && nextTime.isBefore(endTime)) {
                // 放入执行队列
                triggerPoolService.push(info.getId(), nextTime.getSecond());

                // 刷新下次调度时间
                refreshNextValidTime(info, nextTime);
                nextTime = info.getTriggerNextTime();
            }
        }

        // 更新任务
        List<JobInfo> updateJobInfoList = new ArrayList<>(waitTriggerJobs.size());
        LocalDateTime localDateTime = LocalDateTime.now();
        for (JobInfo jobInfo : waitTriggerJobs) {
            JobInfo updateJobInfo = new JobInfo()
                    .setId(jobInfo.getId())
                    .setTriggerStatus(jobInfo.getTriggerStatus())
                    .setTriggerPrevTime(jobInfo.getTriggerPrevTime())
                    .setTriggerNextTime(jobInfo.getTriggerNextTime())
                    .setUpdateTime(localDateTime);
            updateJobInfoList.add(updateJobInfo);
        }
        jobInfoService.updateBatchById(updateJobInfoList);

        // 休眠
        long costMillis = System.currentTimeMillis() - startMillis;
        if (running && costMillis < JOB_SCHEDULE_INTERVAL) {
            Thread.sleep(JOB_SCHEDULE_INTERVAL - costMillis);
        }
    }

    @Override
    protected void afterDoRun() {
    }

    @Override
    protected String getThreadName() {
        return "JobScheduleThread";
    }

    /**
     * 计算任务下次的执行时间
     */
    private void refreshNextValidTime(JobInfo info, LocalDateTime preTriggerTime) {
        CronExpression cronExpression = CronExpression.parse(info.getCron());
        LocalDateTime nextTriggerTime = cronExpression.next(preTriggerTime);
        if (nextTriggerTime == null) {
            // 使任务停止
            info.setTriggerPrevTime(null);
            info.setTriggerNextTime(null);
            info.setTriggerStatus(STOPPED.getValue());
        } else {
            info.setTriggerPrevTime(info.getTriggerNextTime());
            info.setTriggerNextTime(nextTriggerTime);
            info.setTriggerStatus(RUNNING.getValue());
        }
    }

}
