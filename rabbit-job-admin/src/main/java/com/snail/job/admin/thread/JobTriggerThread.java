package com.snail.job.admin.thread;

import com.snail.job.admin.service.trigger.TriggerPoolService;
import com.snail.job.common.thread.RabbitJobAbstractThread;
import java.util.Set;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;
import static com.snail.job.admin.constant.AdminConstants.JOB_TRIGGER_INTERVAL;
import static com.snail.job.common.enums.TriggerType.CRON;

/**
 * 定时任务调度类
 * @author WuQinglong
 */
@Component
public class JobTriggerThread extends RabbitJobAbstractThread {

    @Resource
    private TriggerPoolService triggerPoolService;

    @Override
    protected void execute() {
        long startMillis = System.currentTimeMillis();

        // 当前的秒
        long curSecond = (startMillis / 1000) % 60;

        // 上一秒要执行的任务。1、休眠异常，2、调度时间超过一秒
        Set<Long> preJobIds = triggerPoolService.get((curSecond - 1));
        if (preJobIds != null) {
            preJobIds.forEach(jobId -> triggerPoolService.trigger(jobId, -1, null, CRON));
        }

        // 获取本秒要执行的任务
        Set<Long> jobIds = triggerPoolService.get(curSecond);
        if (jobIds != null) {
            jobIds.forEach(jobId -> triggerPoolService.trigger(jobId, -1, null, CRON));
        }

        // 计算耗时，耗时如果大于1秒会造成任务调度时间不准确
        long costMillis = System.currentTimeMillis() - startMillis;
        if (costMillis >= JOB_TRIGGER_INTERVAL) {
            log.warn("执行任务调度耗时过长：{}毫秒！！！", costMillis);
        }
    }

    @Override
    protected String getThreadName() {
        return "JobTriggerThread";
    }

}
