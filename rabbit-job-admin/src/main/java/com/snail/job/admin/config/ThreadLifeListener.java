package com.snail.job.admin.config;

import com.snail.job.common.constant.ServiceStatus;
import com.snail.job.admin.thread.*;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author WuQinglong
 */
@Component
public class ThreadLifeListener implements ApplicationRunner {

    @Resource
    private ExecutorSweepThread executorSweepThread;
    @Resource
    private JobFailMonitorThread jobFailMonitorThread;
    @Resource
    private JobLogReportThread jobLogReportThread;
    @Resource
    private JobScheduleThread jobScheduleThread;
    @Resource
    private JobTriggerThread jobTriggerThread;

    @Override
    public void run(ApplicationArguments args) {
        executorSweepThread.start();
        jobLogReportThread.start();
        jobFailMonitorThread.start();
        jobScheduleThread.start();
        jobTriggerThread.start();

        // 注册停止
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // 置为服务下线中
            ServiceStatus.status = ServiceStatus.Status.STOPPING;

            executorSweepThread.stop();
            jobLogReportThread.stop();
            jobFailMonitorThread.stop();
            jobScheduleThread.stop();
            jobTriggerThread.stop();
        }));
    }
}
