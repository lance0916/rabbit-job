package com.example.admin.listener;

import com.example.admin.thread.ExecutorSweepThread;
import com.example.admin.thread.JobFailMonitorThread;
import com.example.admin.thread.JobLogReportThread;
import com.example.admin.thread.JobScheduleThread;
import com.example.admin.thread.JobTriggerThread;
import com.example.common.constant.ServiceStatus;
import javax.annotation.Resource;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 控制启动顺序
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
//        jobLogReportThread.start();
//        jobFailMonitorThread.start();
        jobScheduleThread.start();
        jobTriggerThread.start();

        // 注册停止
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // 置为服务下线中
            ServiceStatus.status = ServiceStatus.Status.STOPPING;

            executorSweepThread.stop();
//            jobLogReportThread.stop();
//            jobFailMonitorThread.stop();
            jobScheduleThread.stop();
            jobTriggerThread.stop();
        }));
    }
}
