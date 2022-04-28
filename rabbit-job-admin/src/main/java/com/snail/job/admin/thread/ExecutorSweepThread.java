package com.snail.job.admin.thread;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.snail.job.admin.model.App;
import com.snail.job.admin.model.Executor;
import com.snail.job.admin.service.AppService;
import com.snail.job.admin.service.ExecutorService;
import com.snail.job.common.thread.RabbitJobAbstractThread;
import com.snail.job.common.tools.GsonTool;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;
import static com.snail.job.common.constant.CommonConstants.REGISTER_INTERVAL_TIME;
import static com.snail.job.common.constant.CommonConstants.EXECUTOR_TIME_OUT;

/**
 * 清理无效的执行器
 * @author WuQinglong
 */
@Component
public class ExecutorSweepThread extends RabbitJobAbstractThread {

    @Resource
    private ExecutorService executorService;
    @Resource
    private AppService appService;

    @Override
    public void execute() throws InterruptedException {
        long startMillis = System.currentTimeMillis();

        // 获取所有的执行器
        List<Executor> executors = executorService.list(Wrappers.<Executor>query().eq(Executor.DELETED, 0));

        // 判断执行器的更新时间，是否超过了三个注册间隔时间
        LocalDateTime timeOutDateTime = LocalDateTime.now().minusSeconds(EXECUTOR_TIME_OUT);

        // 过期的执行器集合
        List<Executor> invalidExecutors = new ArrayList<>();
        List<Executor> validExecutors = new ArrayList<>();
        for (Executor executor : executors) {
            LocalDateTime updateTime = executor.getUpdateTime();
            if (timeOutDateTime.isAfter(updateTime)) {
                // 软删除
                executor.setDeleted(1);
                invalidExecutors.add(executor);
            } else {
                validExecutors.add(executor);
            }
        }

        // 批量软删
        executorService.saveOrUpdateBatch(invalidExecutors);
        log.info("清理执行器:{}", GsonTool.toJson(invalidExecutors));

        // 加入更新本地缓存
        Map<String, Set<String>> appNameAddressMap = new HashMap<>();
        for (Executor executor : validExecutors) {
            String appName = executor.getAppName();
            String address = executor.getAddress();
            Set<String> addresses = appNameAddressMap.computeIfAbsent(appName, k -> new HashSet<>());
            addresses.add(address);
        }

        // 查询所有应用集合
        List<App> apps = appService.list();

        // 整理执行器地址
        List<App> updateApps = apps.stream()
                .filter(app -> appNameAddressMap.containsKey(app.getName()))
                .peek(app -> {
                    Set<String> addresses = appNameAddressMap.get(app.getName());
                    app.setAddresses(String.join(",", addresses));
                }).collect(Collectors.toList());
        appService.updateBatchById(updateApps);

        // 休眠
        long costMillis = System.currentTimeMillis() - startMillis;
        if (running && costMillis < REGISTER_INTERVAL_TIME) {
            // 30 秒 - 耗时，每次扫描间隔差不多为 30 秒
            Thread.sleep(REGISTER_INTERVAL_TIME - costMillis);
        }
    }

    @Override
    public String getThreadName() {
        return this.getClass().getSimpleName();
    }

}
