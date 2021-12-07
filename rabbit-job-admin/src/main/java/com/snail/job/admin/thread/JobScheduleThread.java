package com.snail.job.admin.thread;

import com.snail.job.admin.entity.JobInfo;
import com.snail.job.admin.service.trigger.TriggerPoolService;
import com.snail.job.admin.repository.JobInfoRepository;
import com.snail.job.common.thread.RabbitJobAbstractThread;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.snail.job.admin.constant.AdminConstants.JOB_PRE_SCAN_TIME;
import static com.snail.job.admin.constant.AdminConstants.JOB_SCHEDULE_INTERVAL;
import static com.snail.job.common.enums.TriggerType.CRON;
import static com.snail.job.common.enums.TriggerStatus.RUNNING;
import static com.snail.job.common.enums.TriggerStatus.STOPPED;

/**
 * 定时任务调度类
 * @author 吴庆龙
 * @date 2020/7/9 2:40 下午
 */
@Component
public class JobScheduleThread extends RabbitJobAbstractThread {

    @Resource
    private TriggerPoolService triggerPoolService;
    @Resource
    private JobInfoRepository jobInfoRepository;

    @Override
    protected void doRun() throws InterruptedException {
        long startMillis = System.currentTimeMillis();
        long curTimeInSecond = startMillis / 1000;

        // 获取 下次执行时间在 当前时间 + 10秒 之前的所有任务
        long lastTriggerTime = curTimeInSecond + JOB_PRE_SCAN_TIME;

        // 扫描将要待执行的任务，根据任务的执行时间从近到远排序
        List<JobInfo> waitTriggerJobs = jobInfoRepository.findAllWaitTriggerJob(RUNNING.getValue(), lastTriggerTime);

        // 遍历待执行的任务
        for (JobInfo info : waitTriggerJobs) {
            // 任务的下次执行时间
            Long nextTriggerTime = info.getNextTriggerTime();

            // 1、过时的任务，忽略调度并更新下次的执行时间
            if (nextTriggerTime < curTimeInSecond) {
                Instant now = Instant.now(Clock.systemDefaultZone());

                // 刷新下次调度时间
                refreshNextValidTime(info, now.getEpochSecond());
            }
            // 2、当前秒要执行的任务, 直接进行调度
            else if (nextTriggerTime == curTimeInSecond) {
                // 直接进行调度
                triggerPoolService.trigger(info.getId(), -1, null, CRON);

                // 刷新下次调度时间
                refreshNextValidTime(info, nextTriggerTime);
            }

            // 重新获取任务的下次执行时间
            nextTriggerTime = info.getNextTriggerTime();

            // 3、在 [当前秒+1秒，当前时间 + 10秒] 时间内要调度？
            while (nextTriggerTime <= lastTriggerTime && nextTriggerTime != 0) {
                // 任务在第几秒开始执行
                long second = nextTriggerTime % 60;

                // 放入执行队列
                triggerPoolService.push(info.getId(), second);

                // 刷新下次调度时间
                refreshNextValidTime(info, nextTriggerTime);
                nextTriggerTime = info.getNextTriggerTime();
            }
        }

        // 更新任务
        List<JobInfo> updateJobInfoList = new ArrayList<>(waitTriggerJobs.size());
        LocalDateTime localDateTime = LocalDateTime.now();
        for (JobInfo jobInfo : waitTriggerJobs) {
            JobInfo updateJobInfo = JobInfo.builder()
                    .id(jobInfo.getId())
                    .triggerStatus(jobInfo.getTriggerStatus())
                    .prevTriggerTime(jobInfo.getPrevTriggerTime())
                    .nextTriggerTime(jobInfo.getNextTriggerTime())
                    .updateTime(localDateTime)
                    .build();
            updateJobInfoList.add(updateJobInfo);
        }
        jobInfoRepository.saveAllAndFlush(updateJobInfoList);

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
    private void refreshNextValidTime(JobInfo info, long second) {
        Instant instant = Instant.ofEpochSecond(second);
        CronExpression cronExpression = CronExpression.parse(info.getCron());
        Instant nextTriggerTime = cronExpression.next(instant);
        if (nextTriggerTime == null) {
            // 使任务停止
            info.setPrevTriggerTime(0L);
            info.setNextTriggerTime(0L);
            info.setTriggerStatus(STOPPED.getValue());
        } else {
            info.setPrevTriggerTime(info.getNextTriggerTime());
            info.setNextTriggerTime(nextTriggerTime.getEpochSecond());
            info.setTriggerStatus(RUNNING.getValue());
        }
    }

}
