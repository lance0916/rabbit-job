package com.example.admin.route.strategy;

import cn.hutool.core.collection.CollUtil;
import com.example.admin.route.AbstractRoute;
import java.util.HashMap;
import java.util.List;
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
    public String getExecutorAddress(Long jobId, List<String> addresses) {
        if (CollUtil.isEmpty(addresses)) {
            return null;
        }
        Integer lastIndex = executorLastIndex.getOrDefault(jobId, 0);

        // 回到 0，形成循环
        if (lastIndex >= addresses.size()) {
            lastIndex = 0;
        }
        String address = addresses.get(lastIndex);

        // 轮训更新
        executorLastIndex.put(jobId, lastIndex + 1);
        return address;
    }
}
