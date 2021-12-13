package com.snail.job.admin.route.strategy;

import cn.hutool.core.util.HashUtil;
import com.snail.job.admin.route.RouterStrategy;

import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 一致性hash
 * @author 吴庆龙
 * @date 2020/6/17 10:11 上午
 */
public class ConsistentHashRoute extends RouterStrategy {

    /**
     * 每个真实节点对应虚拟节点的数量
     */
    private final static int V_NODE_NUM = 1000;

    @Override
    public String route(Long jobId, String[] addresses) {
        return hashJob(jobId, addresses);
    }

    public String hashJob(Long jobId, String[] addressList) {
        TreeMap<Integer, String> addressRing = new TreeMap<>();
        for (String address : addressList) {
            for (int i = 0; i < V_NODE_NUM; i++) {
                int addressHash = hash(address + "-V_NODE-" + i);
                addressRing.put(addressHash, address);
            }
        }

        int jobHash = hash(String.valueOf(jobId));
        SortedMap<Integer, String> lastRing = addressRing.tailMap(jobHash);
        if (lastRing.isEmpty()) {
            return addressRing.firstEntry().getValue();
        }
        return lastRing.get(lastRing.firstKey());
    }

    /**
     * Hash算法
     */
    private static int hash(String key) {
        return HashUtil.fnvHash(key);
    }

}