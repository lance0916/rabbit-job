package com.snail.job.admin.route;

import com.snail.job.admin.route.strategy.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 任务路由策略
 * @author 吴庆龙
 * @date 2020/6/4 11:23 上午
 */
public enum RouteStrategyEnum {

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
    ;

    private final String name;
    private final String desc;
    private final RouterStrategy routerStrategy;

    RouteStrategyEnum(String name, String desc, RouterStrategy routerStrategy) {
        this.name = name;
        this.desc = desc;
        this.routerStrategy = routerStrategy;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public RouterStrategy getRouterStrategy() {
        return routerStrategy;
    }

    private static final Map<String, RouterStrategy> strategyMap = new HashMap<>();

    static {
        for (RouteStrategyEnum value : values()) {
            strategyMap.put(value.name, value.routerStrategy);
        }
    }

    public static RouterStrategy match(String name) {
        return strategyMap.get(name);
    }

}
