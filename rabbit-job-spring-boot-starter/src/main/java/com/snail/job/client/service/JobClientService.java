package com.snail.job.client.service;

import com.snail.job.common.model.IdleBeatParam;
import com.snail.job.common.model.ResultT;
import com.snail.job.common.model.TriggerParam;
import com.snail.job.client.handler.IJobHandler;
import com.snail.job.client.helper.ThreadHelper;
import com.snail.job.client.thread.JobThread;
import org.springframework.stereotype.Component;

import static com.snail.job.client.constant.JobConstants.HANDLER_REPOSITORY;
import static com.snail.job.client.constant.JobConstants.THREAD_REPOSITORY;

/**
 * 执行器接口逻辑的实现
 * @author 吴庆龙
 * @date 2020/5/25 2:24 下午
 */
@Component
public class JobClientService {

    /**
     * 心跳检测
     */
    public ResultT<String> beat() {
        return ResultT.SUCCESS;
    }

    /**
     * 忙碌检测
     */
    public ResultT<String> idleBeat(IdleBeatParam idleBeatParam) {
        JobThread jobThread = THREAD_REPOSITORY.get(idleBeatParam.getJobId());
        if (jobThread != null && jobThread.isRunningOrHasQueue()) {
            return new ResultT<>(ResultT.EXECUTOR_BUSY, "执行器忙碌");
        }
        return ResultT.SUCCESS;
    }

    /**
     * 执行任务
     */
    public ResultT<String> run(TriggerParam triggerParam) {
        // 获取任务指定的处理器
        String jobName = triggerParam.getExecHandler();
        IJobHandler jobHandler = HANDLER_REPOSITORY.get(jobName);
        if (jobHandler == null) {
            return new ResultT<>(ResultT.FAIL_CODE, "没有对应的任务方法，jobName=" + jobName);
        }

        // 获取执行线程
        Long jobId = triggerParam.getJobId();
        JobThread jobThread = THREAD_REPOSITORY.get(jobId);

        // 没有线程，创建并启动一个
        if (jobThread == null) {
            jobThread = ThreadHelper.startJobThread(jobId, jobHandler);
        }

        // 判断执行方法是否是一个
        IJobHandler threadJobHandler = jobThread.getJobHandler();
        if (jobHandler != threadJobHandler) {
            // 将之前的线程停了，重新创建一个
            ThreadHelper.stopJobThread(jobId, "JobHandler发生变化");
            jobThread = ThreadHelper.startJobThread(jobId, jobHandler);
        }

        // 添加任务到队列中等待执行
        return jobThread.addToQueue(triggerParam);
    }

}
