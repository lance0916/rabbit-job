package com.snail.job.admin.thread;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.snail.job.admin.model.JobInfo;
import com.snail.job.admin.service.JobInfoService;
import com.snail.job.admin.service.trigger.CronExpression;
import com.snail.job.admin.service.trigger.TriggerPoolService;
import com.snail.job.common.thread.RabbitJobAbstractThread;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;
import static com.snail.job.admin.constant.AdminConstants.JOB_PRE_SCAN_TIME;
import static com.snail.job.admin.constant.AdminConstants.JOB_SCHEDULE_INTERVAL;
import static com.snail.job.common.enums.TriggerStatus.RUNNING;
import static com.snail.job.common.enums.TriggerStatus.STOPPED;
import static com.snail.job.common.enums.TriggerType.CRON;

/**
 * 定时任务调度类
 * @author WuQinglong
 */
@Component
public class JobScheduleThread extends RabbitJobAbstractThread {

    @Resource
    private TriggerPoolService triggerPoolService;
    @Resource
    private JobInfoService jobInfoService;

    @Override
    protected void execute() throws InterruptedException {
        long startMillis = System.currentTimeMillis();

        // 计算本次的结束时间
        long scanStopMillis = startMillis + JOB_PRE_SCAN_TIME;
        Date scanStopDate = new Date(scanStopMillis);

        // 扫描将要待执行的任务，根据任务的执行时间从近到远排序
        List<JobInfo> waitTriggerJobs = jobInfoService.list(Wrappers.<JobInfo>query()
                .eq(JobInfo.TRIGGER_STATUS, RUNNING.getValue())
                .le(JobInfo.TRIGGER_NEXT_TIME, scanStopDate)
                .orderByDesc(JobInfo.TRIGGER_NEXT_TIME)
        );
        if (CollUtil.isEmpty(waitTriggerJobs)) {
            return;
        }

        // 当前的毫秒数，去掉了毫秒精度
        long curMillis = startMillis / 1000 * 1000;

        // 遍历待执行的任务
        for (JobInfo info : waitTriggerJobs) {
            // 任务的下次执行时间，这里的时间应该没有毫秒精度
            Long nextMillis = info.getTriggerNextTime();

            // 1、过时的任务，忽略调度并更新下次的执行时间
            if (nextMillis < curMillis) {
                refreshNextValidTime(info, curMillis);
            }

            // 2、当前秒要执行的任务, 直接进行调度
            else if (nextMillis == curMillis) {
                // 直接进行调度
                triggerPoolService.trigger(info.getId(), -1, null, CRON);

                refreshNextValidTime(info, nextMillis);
            }

            // 重新获取任务的下次执行时间
            nextMillis = info.getTriggerNextTime();

            // 3、在 [当前秒+1秒，当前时间 + xxx秒) 时间内要调度
            while (nextMillis < scanStopMillis) {
                // 放入执行队列
                triggerPoolService.push(info.getId(), nextMillis / 1000);

                // 刷新下次调度时间
                refreshNextValidTime(info, nextMillis);
                nextMillis = info.getTriggerNextTime();
            }
        }

        // 更新任务
        jobInfoService.updateBatchById(waitTriggerJobs);

        // 休眠
        long costMillis = System.currentTimeMillis() - startMillis;
        if (running && costMillis < JOB_SCHEDULE_INTERVAL) {
            Thread.sleep(JOB_SCHEDULE_INTERVAL - costMillis);
        }
    }

    @Override
    protected String getThreadName() {
        return "JobScheduleThread";
    }

    /**
     * 计算任务下次的执行时间
     */
    private void refreshNextValidTime(JobInfo info, long millis) {
        // 这里表达式不会有误的，除非手工改动数据
        Date nextDate = null;
        try {
            CronExpression cronExpression = new CronExpression(info.getCron());
            nextDate = cronExpression.getNextValidTimeAfter(new Date(millis));
        } catch (Exception e) {
            log.error("任务:{} 的 Cron 表达式有误:{}", info.getId(), info.getCron());
        }
        if (nextDate == null) {
            // 使任务停止
            info.setTriggerStatus(STOPPED.getValue());
        } else {
            long nextTime = nextDate.getTime();

            // 刷新下次调用时间
            info.setTriggerPrevTime(info.getTriggerNextTime());
            info.setTriggerNextTime(nextTime / 1000 * 1000);
            info.setTriggerStatus(RUNNING.getValue());
        }
    }

}
