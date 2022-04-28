package com.snail.job.client.config;

import com.snail.job.client.thread.CallbackThread;
import com.snail.job.client.thread.RegisterThread;
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
        callbackThread.start();

        // 注册关闭钩子
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            callbackThread.stop();
            registerThread.stop();
        }, "JobClientCloseThread"));
    }

}
