package com.snail.job.client.listener;

import com.snail.job.client.thread.CallbackThread;
import com.snail.job.client.thread.RegisterThread;
import com.snail.job.common.constant.ServiceStatus;
import java.util.concurrent.TimeUnit;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author WuQinglong
 */
@Component
public class JobListener implements ApplicationRunner {

    @Resource
    private CallbackThread callbackThread;
    @Resource
    private RegisterThread registerThread;
    @Resource
    private JobScanner jobScanner;

    /**
     * 启动线程
     */
    @Override
    public void run(ApplicationArguments args) {
        // 注册任务
        jobScanner.scanAndRegister();

        // 启动守护线程
        registerThread.start();
//        callbackThread.start();

        // 注册关闭钩子
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // 置为服务下线中
            ServiceStatus.status = ServiceStatus.Status.STOPPING;
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                // ignore
            }

//            callbackThread.stop();
            registerThread.stop();
        }, "JobClientCloseThread"));
    }

}
