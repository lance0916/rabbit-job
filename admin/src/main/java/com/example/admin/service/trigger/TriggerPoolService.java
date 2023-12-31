package com.example.admin.service.trigger;

import com.example.common.enums.TriggerType;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 将任务的调度放在线程池里进行
 * @author WuQinglong
 */
@Slf4j
@Component
public class TriggerPoolService {
    // ---------------------------------- 调度缓存 BEGIN

    /**
     * 缓存将要执行的任务
     * key: 秒
     * val: 任务ID集合
     */
    private final Map<Long, Set<Long>> triggerJobMap = new ConcurrentHashMap<>();

    /**
     * 添加调度到缓存中
     */
    public void push(Long jobId, Long invokeSecond) {
        Set<Long> jobIdSet = triggerJobMap.computeIfAbsent(invokeSecond, k -> new HashSet<>());
        jobIdSet.add(jobId);
    }

    /**
     * 从缓存中移除
     */
    public Set<Long> get(Long second) {
        return triggerJobMap.remove(second);
    }

    // ---------------------------------- 调度缓存 END

    // ---------------------------------- 任务调度 BEGIN

    @Resource
    private JobTriggerService jobTriggerService;
    @Resource(name = "triggerThreadPool")
    private ThreadPoolExecutor triggerThreadPool;

    /**
     * 添加任务到线程池中调度
     * @param jobId                  任务的id
     * @param triggerType            触发类型
     * @param overrideFailRetryCount 如果指定则使用该值，-1表示不指定
     * @param overrideExecParam      如果指定则使用改制，null表示不指定
     */
    public void trigger(Long jobId, Integer overrideFailRetryCount, String overrideExecParam, TriggerType triggerType) {
        triggerThreadPool.execute(() -> {
            jobTriggerService.trigger(jobId, overrideFailRetryCount, overrideExecParam, triggerType);
        });
    }

    // ---------------------------------- 任务调度 END

}
