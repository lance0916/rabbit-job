package com.snail.job.admin.thread;

import com.snail.job.admin.entity.Application;
import com.snail.job.admin.entity.Executor;
import com.snail.job.admin.repository.ApplicationRepository;
import com.snail.job.admin.repository.ExecutorRepository;
import com.snail.job.common.thread.RabbitJobAbstractThread;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.snail.job.common.constant.CommonConstants.BEAT_TIME;
import static com.snail.job.common.constant.CommonConstants.BEAT_TIME_OUT;

/**
 * 清理无效的执行器
 * @author 吴庆龙
 * @date 2020/6/4 11:23 上午
 */
@Component
public class ExecutorSweepThread extends RabbitJobAbstractThread {

    @Resource
    private ExecutorRepository executorRepository;
    @Resource
    private ApplicationRepository applicationRepository;

    @Override
    public void doRun() throws InterruptedException {
        long startMillis = System.currentTimeMillis();

        // 获取所有的执行器
        List<Executor> executors = executorRepository.findAll();

        // 判断执行器的更新时间，是否超过了三个注册间隔时间
        LocalDateTime timeOutDateTime = LocalDateTime.now().minusSeconds(BEAT_TIME_OUT);

        // 过期的执行器集合
        List<Executor> invalidExecutors = new ArrayList<>();
        List<Executor> validExecutors = new ArrayList<>();
        for (Executor executor : executors) {
            LocalDateTime updateTime = executor.getUpdateTime();
            if (timeOutDateTime.isAfter(updateTime)) {
                // 软删除
                executor.setDeleted((byte) 1);
                invalidExecutors.add(executor);

                // 清空不需要更新的属性
                executor.setAppName(null);
                executor.setAddress(null);
                executor.setCreateTime(null);
                executor.setUpdateTime(null);
            } else {
                validExecutors.add(executor);
            }
        }

        // 批量更新，执行软删
        executorRepository.saveAllAndFlush(invalidExecutors);

        // 加入更新本地缓存
        Map<String, Set<String>> appNameAddressMap = new HashMap<>();
        for (Executor executor : validExecutors) {
            String appName = executor.getAppName();
            String address = executor.getAddress();
            Set<String> addresses = appNameAddressMap.computeIfAbsent(appName, k -> new HashSet<>());
            addresses.add(address);
        }

        // 查询所有应用集合
        List<Application> applications = applicationRepository.findAll();

        // 整理执行器地址
        LocalDateTime updateLocalTime = LocalDateTime.now();
        List<Application> updateApplicationList = applications.stream()
                .filter(app -> appNameAddressMap.containsKey(app.getName()))
                .peek(app -> {
                    Set<String> addresses = appNameAddressMap.get(app.getName());
                    app.setAddresses(String.join(",", addresses));
                    app.setUpdateTime(updateLocalTime);

                    // 清空不需要更新的属性
                    app.setName(null);
                    app.setType(null);
                    app.setCreateTime(null);
                }).collect(Collectors.toList());
        // 更新执行器地址
        applicationRepository.saveAllAndFlush(updateApplicationList);

        // 休眠
        long costMillis = System.currentTimeMillis() - startMillis;
        if (running && costMillis < BEAT_TIME) {
            // 30 秒 - 耗时，每次扫描间隔差不多为 30 秒
            Thread.sleep(BEAT_TIME - costMillis);
        }
    }

    @Override
    public String getThreadName() {
        return this.getClass().getSimpleName();
    }

}
