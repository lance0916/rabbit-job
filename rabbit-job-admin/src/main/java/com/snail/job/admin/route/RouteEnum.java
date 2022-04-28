package com.snail.job.admin.route;

import com.snail.job.admin.route.strategy.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 任务路由策略
 * @author 吴庆龙
 */
public enum RouteEnum {

    /**
     * 第一个
     */
    FIRST("first", "第一个", new FirstRoute()),

    /**
     * 最后一个
     */
    LAST("last", "最后一个", new LastRoute()),

    /**
     * 一致性hash
     */
    CONSISTENT_HASH("consistent_hash", "一致性HASH", new ConsistentHashRoute()),

    /**
     * 故障转移
     */
    FAIL_OVER("fail_over", "故障转移", new FailOverRoute()),

    /**
     * 忙碌转移
     */
    BUSY_OVER("busy_over", "忙碌转移", new BusyOverRoute()),

    /**
     * 广播
     */
    BROADCAST("broadcast", "广播", null),

    /**
     * 轮训
     */
    RoundRobin("round_robin", "轮训", new RoundRobinRoute()),
    ;

    private final String name;
    private final String desc;
    private final AbstractRoute abstractRoute;

    RouteEnum(String name, String desc, AbstractRoute abstractRoute) {
        this.name = name;
        this.desc = desc;
        this.abstractRoute = abstractRoute;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public AbstractRoute getRouterStrategy() {
        return abstractRoute;
    }

    private static final Map<String, AbstractRoute> strategyMap = new HashMap<>();

    static {
        for (RouteEnum value : values()) {
            strategyMap.put(value.name, value.abstractRoute);
        }
    }

    public static AbstractRoute match(String name) {
        return strategyMap.get(name);
    }

}
