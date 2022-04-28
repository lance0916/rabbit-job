package com.snail.job.admin.config;

import com.snail.job.common.thread.JobUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 控制启动顺序
 * @author 吴庆龙
 */
@Component
public class ThreadPoolConfig {

    /**
     * Api接口使用的线程池
     */
    @Bean(name = "apiThreadPool", destroyMethod = "shutdown")
    public ThreadPoolExecutor apiThreadPool() {
        return new ThreadPoolExecutor(
                50, 100, 60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(100),
                new ThreadFactory() {
                    private final AtomicInteger threadNumber = new AtomicInteger(1);

                    @Override
                    public Thread newThread(Runnable r) {
                        Thread thread = new Thread(r);
                        thread.setName("ApiPool-" + threadNumber.getAndIncrement());
                        thread.setUncaughtExceptionHandler(new JobUncaughtExceptionHandler());
                        return thread;
                    }
                }
        );
    }

    /**
     * 调度线程池配置
     * 100个线程，每次调度耗时约 200 毫秒，则一秒内可以调度 100 * 200 约等于 20000 个任务
     * 加上线程切换耗时，每秒约能调度 18000 个任务
     */
    @Bean(name = "triggerThreadPool", destroyMethod = "shutdown")
    public ThreadPoolExecutor triggerThreadPool() {
        return new ThreadPoolExecutor(
                100, 200, 60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(1000),
                new ThreadFactory() {
                    private final AtomicInteger threadNumber = new AtomicInteger(1);

                    @Override
                    public Thread newThread(Runnable r) {
                        Thread thread = new Thread(r);
                        thread.setName("TriggerPool-" + threadNumber.getAndIncrement());
                        thread.setUncaughtExceptionHandler(new JobUncaughtExceptionHandler());
                        return thread;
                    }
                }
        );
    }

}
