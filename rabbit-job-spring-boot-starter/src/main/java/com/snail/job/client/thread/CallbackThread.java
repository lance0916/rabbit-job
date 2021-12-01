package com.snail.job.client.thread;

import com.snail.job.client.biz.AdminBiz;
import com.snail.job.common.model.CallbackParam;
import com.snail.job.common.thread.RabbitJobAbstractThread;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 发送任务执行结果给调度中心
 * @author 吴庆龙
 * @date 2020/5/26 4:43 下午
 */
@Component
public class CallbackThread extends RabbitJobAbstractThread {

    @Resource
    private AdminBiz adminBiz;

    /**
     * 回调队列
     */
    private static final LinkedBlockingQueue<CallbackParam> QUEUE = new LinkedBlockingQueue<>();

    /**
     * 添加回调任务到队列中
     */
    public static void addCallbackQueue(CallbackParam callbackParam) {
        QUEUE.add(callbackParam);
    }

    @Override
    protected void doRun() throws InterruptedException {
        CallbackParam callbackParam = QUEUE.take();

        // 一次性最大回调 50 个执行结果
        List<CallbackParam> callbackParamList = new ArrayList<>(50);
        QUEUE.drainTo(callbackParamList, 49);
        callbackParamList.add(callbackParam);

        adminBiz.callback(callbackParamList);
    }

    @Override
    protected void destroy() {
        List<CallbackParam> callbackParamList = new ArrayList<>(QUEUE.size());
        QUEUE.drainTo(callbackParamList);

        adminBiz.callback(callbackParamList);
    }

    @Override
    protected String getThreadName() {
        return "CallBackThread";
    }

}
