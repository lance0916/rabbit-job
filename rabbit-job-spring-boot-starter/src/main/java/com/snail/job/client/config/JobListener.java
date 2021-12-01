package com.snail.job.client.config;

import com.snail.job.client.thread.CallbackThread;
import com.snail.job.client.thread.RegisterThread;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author WuQinglong
 * @date 2021/9/22 7:48 下午
 */
@Component
public class JobListener implements ApplicationRunner {

    @Resource
    private CallbackThread callbackThread;
    @Resource
    private RegisterThread registerThread;

    /**
     * 启动线程
     */
    @Override
    public void run(ApplicationArguments args) {
        registerThread.start();
        callbackThread.start();

        // 注册关闭钩子
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            callbackThread.stop();
            registerThread.stop();
        }, "JobClientCloseThread"));
    }

}
