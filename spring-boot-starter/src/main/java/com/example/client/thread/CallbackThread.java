package com.example.client.thread;

import com.example.client.biz.AdminBiz;
import com.example.common.model.CallbackParam;
import com.example.common.RabbitJobAbstractThread;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 发送任务执行结果给调度中心
 * @author WuQinglong
 */
@Component
public class CallbackThread extends RabbitJobAbstractThread {

    @Resource
    private AdminBiz adminBiz;

    /**
     * 回调队列
     */
    private static final LinkedBlockingQueue<CallbackParam> callbackQueue = new LinkedBlockingQueue<>();

    /**
     * 添加回调任务到队列中
     */
    public static void addCallbackQueue(CallbackParam callbackParam) {
        callbackQueue.add(callbackParam);
    }

    @Override
    protected void execute() throws InterruptedException {
        CallbackParam callbackParam = callbackQueue.take();

        // 一次性最大回调 50 个执行结果
        List<CallbackParam> callbackParamList = new ArrayList<>(50);
        callbackQueue.drainTo(callbackParamList, 49);
        callbackParamList.add(callbackParam);

        adminBiz.callback(callbackParamList);
    }

    @Override
    protected void after() {
        List<CallbackParam> callbackParamList = new ArrayList<>(callbackQueue.size());
        callbackQueue.drainTo(callbackParamList);

        adminBiz.callback(callbackParamList);
    }

    @Override
    protected String getThreadName() {
        return "CallBackThread";
    }

}
