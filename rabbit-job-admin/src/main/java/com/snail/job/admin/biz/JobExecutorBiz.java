package com.snail.job.admin.biz;

import com.snail.job.common.model.IdleBeatParam;
import com.snail.job.common.model.ResultT;
import com.snail.job.common.model.TriggerParam;
import com.snail.job.common.proxy.ExecutorProxy;
import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author WuQinglong
 */
@Component
public class JobExecutorBiz {

    @Value("${rabbit-job.admin.secret-key}")
    private String secretKey;

    /**
     * 执行器缓存
     */
    private final HashMap<String, ExecutorProxy> executorBizRepository = new HashMap<>();

    /**
     * 生成对象的锁
     */
    private final Lock lock = new ReentrantLock();

    /**
     * 获取执行器
     */
    private ExecutorProxy getExecutorBiz(String address) {
        ExecutorProxy executorBiz = executorBizRepository.get(address);
        if (executorBiz == null) {
            lock.lock();
            try {
                executorBiz = executorBizRepository.get(address);
                if (executorBiz == null) {
                    executorBiz = new ExecutorProxy(address, secretKey);
                    executorBizRepository.put(address, executorBiz);
                }
            } finally {
                lock.unlock();
            }
        }
        return executorBiz;
    }

    /**
     * 心跳监测
     */
    public ResultT<String> beat(String address) {
        return getExecutorBiz(address).beat();
    }

    /**
     * 忙碌监测
     */
    public ResultT<String> idleBeat(String address, IdleBeatParam idleBeatParam) {
        return getExecutorBiz(address).idleBeat(idleBeatParam);
    }

    /**
     * 执行任务
     */
    public ResultT<String> run(String address, TriggerParam triggerParam) {
        return getExecutorBiz(address).run(triggerParam);
    }

}
