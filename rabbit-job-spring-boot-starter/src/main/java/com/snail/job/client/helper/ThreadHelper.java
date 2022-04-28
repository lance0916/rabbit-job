package com.snail.job.client.helper;

import com.snail.job.common.thread.JobUncaughtExceptionHandler;
import com.snail.job.client.handler.IJobHandler;
import com.snail.job.client.thread.JobThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicInteger;

import static com.snail.job.common.tools.StrTool.stringifyException;
import static com.snail.job.client.constant.JobConstants.THREAD_REPOSITORY;

/**
 * @author WuQinglong
 */
public class ThreadHelper {
    private static final Logger log = LoggerFactory.getLogger(ThreadHelper.class);

    private static final AtomicInteger THREAD_NUMBER = new AtomicInteger(1);

    /**
     * 创建线程并启动
     */
    public static JobThread startJobThread(Long jobId, IJobHandler jobHandler) {
        JobThread thread = new JobThread(jobId, jobHandler);
        thread.setName("JobThread-" + jobId);
        thread.setUncaughtExceptionHandler(new JobUncaughtExceptionHandler());
        thread.start();
        return thread;
    }

    /**
     * 停止线程
     */
    public static void stopJobThread(Long jobId, String reason) {
        if (reason != null && reason.length() > 0) {
            log.info("停止线程。jobId={} reason:{}", jobId, reason);
        }
        JobThread thread = THREAD_REPOSITORY.remove(jobId);
        if (thread == null) {
            return;
        }
        thread.setRunning(false);
        thread.interrupt();
        try {
            thread.join();
        } catch (InterruptedException e) {
            log.error("停止线程。异常={}", stringifyException(e));
        }
    }

    /**
     * 创建Feature线程并启动
     */
    public static Thread startJobFeatureThread(FutureTask<Void> futureTask) {
        Thread thread = new Thread(futureTask);
        thread.setName("FutureJobThread-" + THREAD_NUMBER.getAndIncrement());
        thread.setUncaughtExceptionHandler(new JobUncaughtExceptionHandler());
        thread.start();
        return thread;
    }

}
