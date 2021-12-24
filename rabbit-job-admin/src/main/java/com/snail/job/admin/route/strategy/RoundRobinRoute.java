package com.snail.job.admin.route.strategy;

import com.snail.job.admin.route.AbstractRoute;

import java.util.HashMap;
import java.util.Map;

/**
 * 轮训
 * @author WuQinglong
 * @since 2021/12/24 8:50 上午
 */
public class RoundRobinRoute extends AbstractRoute {

    /**
     * 每个执行器的地址
     */
    private static final Map<Long, Integer> executorLastIndex = new HashMap<>();

    @Override
    public String getExecutorAddress(Long appId, Long jobId, String[] addresses) {
        Integer lastIndex = executorLastIndex.getOrDefault(appId, 0);

        // 回到 0，形成循环
        if (lastIndex >= addresses.length) {
            lastIndex = 0;
        }
        String address = addresses[lastIndex];

        // 轮训更新
        executorLastIndex.put(appId, lastIndex + 1);

        return address;
    }
}
