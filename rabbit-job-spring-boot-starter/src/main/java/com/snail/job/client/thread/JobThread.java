package com.snail.job.client.thread;

import com.snail.job.common.model.CallbackParam;
import com.snail.job.common.model.ResultT;
import com.snail.job.common.model.TriggerParam;
import com.snail.job.client.handler.IJobHandler;
import com.snail.job.client.helper.ThreadHelper;
import com.snail.job.client.helper.JobContext;
import com.snail.job.client.helper.JobHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.snail.job.common.tools.StrTool.stringifyException;

/**
 * 每个任务都会对应一个线程
 * @author 吴庆龙
 * @date 2020/5/26 12:22 下午
 */
public class JobThread extends Thread {

    public final Logger log = LoggerFactory.getLogger(JobThread.class);

    /**
     * 任务的 id
     */
    private final Long jobId;

    /**
     * 任务的执行逻辑
     */
    private final IJobHandler jobHandler;

    /**
     * 该线程要执行的任务队列
     */
    private final ArrayBlockingQueue<TriggerParam> jobQueue;

    /**
     * 线程运行标识
     */
    private volatile boolean running = true;

    /**
     * 是否正在执行任务
     */
    private volatile boolean runningTask = false;

    /**
     * 线程空闲次数统计
     */
    private int idleTimesCount = 0;

    /**
     * 线程空闲次数阈值
     * TODO 后期将该参数开放
     */
    private int idleTimes = 30;

    /**
     * 线程队列的长度
     * TODO 后期将该参数开放
     */
    private int queueSize = 10;

    public JobThread(Long jobId, IJobHandler jobHandler) {
        this.jobId = jobId;
        this.jobHandler = jobHandler;
        this.jobQueue = new ArrayBlockingQueue<>(queueSize);
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    /**
     * 获取 JobHandler
     */
    public IJobHandler getJobHandler() {
        return jobHandler;
    }

    /**
     * 添加任务到任务队列中
     */
    public ResultT<String> addToQueue(TriggerParam param) {
        if (!running) {
            return new ResultT<>(ResultT.FAIL_CODE, "执行器线程处于未运行状态！！！");
        }

        // 添加到执行队列
        try {
            jobQueue.add(param);
        } catch (Exception e) {
            return new ResultT<>(ResultT.FAIL_CODE, "执行器队列已满");
        }

        return ResultT.SUCCESS;
    }

    /**
     * JobThread 是否忙碌
     */
    public boolean isRunningOrHasQueue() {
        if (running) {
            return runningTask || jobQueue.size() > 0;
        }
        return false;
    }

    @Override
    public void run() {
        // 执行该 JobHandler 的初始化方法
        try {
            jobHandler.init();
        } catch (Exception e) {
            log.error("执行任务初始化方法异常。{}", stringifyException(e));
            return;
        }

        // 线程启动后不断轮训队列，有任务就执行，没有任务则累加次数达到 30 次就将该线程停止
        while (running) {
            try {
                // 累加无任务次数
                idleTimesCount++;

                // 从队列中获取任务
                TriggerParam triggerParam = null;
                try {
                    triggerParam = jobQueue.poll(5L, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    running = false;
                    log.warn("线程被终止！！！");
                }

                // 无任务，30 次获取操作后，没有任务可以处理，则自动销毁线程
                if (triggerParam == null) {
                    if (idleTimesCount > idleTimes && jobQueue.size() == 0) {
                        running = false;
                        ThreadHelper.stopJobThread(jobId, "执行线程空闲，主动终止");
                    }
                    continue;
                }

                // 在执行任务
                runningTask = true;
                // 重置无任务次数
                idleTimesCount = 0;

                // 使用线程共享变量传递参数
                JobContext context = new JobContext(
                        triggerParam.getJobId(),
                        triggerParam.getExecParam(),
                        null,
                        triggerParam.getShardIndex(),
                        triggerParam.getShardTotal()
                );
                JobHelper.setContext(context);

                // 执行任务方法
                LocalDateTime startInvokeTime = LocalDateTime.now();
                try {
                    doInvoke(triggerParam);
                } catch (Exception e) {
                    String errorMsg = stringifyException(e);
                    log.error("任务执行异常. {}", errorMsg);

                    // 设置执行结果
                    context.setHandleCode(ResultT.FAIL_CODE);
                    context.setHandleMsg(errorMsg);
                }
                LocalDateTime stopInvokeTime = LocalDateTime.now();

                // 进行任务执行结果的回调
                CallbackParam callbackParam = new CallbackParam(
                        triggerParam.getLogId(),
                        context.getHandleCode(),
                        context.getHandleMsg(),
                        startInvokeTime,
                        stopInvokeTime
                );
                CallbackThread.addCallbackQueue(callbackParam);

                // 移除
                JobHelper.removeContent();

                // 设置线程执行状态
                runningTask = false;
            } catch (Exception e) {
                log.error("未知异常。{}", stringifyException(e));
            }
        }

        // 线程被终止，未执行的任务会以失败的回调反馈给调度中心
        while (jobQueue.size() > 0) {
            TriggerParam triggerParam = jobQueue.poll();
            if (triggerParam == null) {
                continue;
            }
            CallbackParam callbackParam = new CallbackParam(
                    triggerParam.getLogId(),
                    ResultT.JOB_UN_EXEC_CODE,
                    "线程被终止，任务还未进行调度。",
                    null,
                    null
            );
            CallbackThread.addCallbackQueue(callbackParam);
        }

        // 执行该 JobHandler 的销毁方法
        try {
            jobHandler.destroy();
        } catch (Exception e) {
            log.error("执行任务销毁方法异常。{}", stringifyException(e));
        }
    }

    /**
     * 执行方法
     */
    private void doInvoke(TriggerParam triggerParam) throws Exception {
        // 获取超时时间
        Integer executorTimeout = triggerParam.getExecTimeout();

        // 无超时时间
        if (executorTimeout == null || executorTimeout <= 0) {
            jobHandler.execute();
            return;
        }

        // 有超时时间
        FutureTask<Void> futureTask = new FutureTask<>(() -> {
            jobHandler.execute();
            return null;
        });
        Thread futureThread = ThreadHelper.startJobFeatureThread(futureTask);
        try {
            futureTask.get(executorTimeout, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            JobHelper.setFail(ResultT.TIMEOUT_CODE, "任务执行超时！！！");

            // 设置中断
            futureThread.interrupt();
        } catch (InterruptedException e) {
            JobHelper.setFail(ResultT.JOB_ABORT_CODE, "任务被终止！！！");
        }
    }

}
