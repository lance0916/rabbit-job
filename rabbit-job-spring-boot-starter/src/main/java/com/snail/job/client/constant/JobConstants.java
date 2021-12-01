package com.snail.job.client.constant;

import com.snail.job.client.handler.IJobHandler;
import com.snail.job.client.thread.JobThread;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author WuQinglong
 * @date 2021/9/5 5:27 下午
 */
public interface JobConstants {

    /**
     * 任务处理器
     * key: 任务名
     * value: 处理器
     */
    ConcurrentHashMap<String, IJobHandler> HANDLER_REPOSITORY = new ConcurrentHashMap<>();

    /**
     * 任务对应的线程
     * key: 任务id
     * value: 线程
     */
    ConcurrentHashMap<Long, JobThread> THREAD_REPOSITORY = new ConcurrentHashMap<>();

}
