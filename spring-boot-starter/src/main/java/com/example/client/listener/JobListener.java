package com.example.client.listener;

import com.example.client.thread.CallbackThread;
import com.example.client.thread.RegisterThread;
import com.example.common.constant.ServiceStatus;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

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

//            callbackThread.stop();
            registerThread.stop();

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                // ignore
            }
        }, "JobClientCloseThread"));
    }

}
