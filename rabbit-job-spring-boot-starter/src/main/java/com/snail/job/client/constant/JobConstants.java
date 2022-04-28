package com.snail.job.client.constant;

import com.snail.job.client.handler.IJobHandler;
import com.snail.job.client.thread.JobThread;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author WuQinglong
 */
public interface JobConstants {

    /**
     * 任务处理器
     * key: 任务名
     * value: 处理器
     */
    ConcurrentHashMap<String, IJobHandler> handlerRepository = new ConcurrentHashMap<>();

    /**
     * 任务对应的线程
     * key: 任务id
     * value: 线程
     */
    ConcurrentHashMap<Long, JobThread> threadRepository = new ConcurrentHashMap<>();

}
