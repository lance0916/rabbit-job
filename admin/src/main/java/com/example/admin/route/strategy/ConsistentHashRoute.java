package com.example.admin.route.strategy;

import cn.hutool.core.util.HashUtil;
import com.example.admin.route.AbstractRoute;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 一致性hash
 * @author WuQinglong
 */
public class ConsistentHashRoute extends AbstractRoute {

    /**
     * 每个真实节点对应虚拟节点的数量
     */
    private final static int V_NODE_NUM = 1000;

    @Override
    public String getExecutorAddress(Long jobId, List<String> addresses) {
        return hashJob(jobId, addresses);
    }

    public String hashJob(Long jobId, List<String> addressList) {
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
