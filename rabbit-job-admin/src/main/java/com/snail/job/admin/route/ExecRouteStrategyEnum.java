package com.snail.job.admin.route;

/**
 * 任务路由策略
 * @author 吴庆龙
 * @date 2020/6/4 11:23 上午
 */
public enum ExecRouteStrategyEnum {

    /**
     * 第一个
     */
    FIRST("first", "第一个"),

    /**
     * 最后一个
     */
    LAST("last", "最后一个"),

//    ROUND("round", (byte) 3, "轮询", new ExecutorRouteRound()),
//    RANDOM("random", (byte) 4, "随机", new ExecutorRouteRandom()),
//    CONSISTENT_HASH("consistent_hash", (byte) 5, "一致性HASH", new ExecutorRouteConsistentHash()),
//    LEAST_FREQUENTLY_USED("least_frequently_used", (byte) 6, "最不经常使用", new ExecutorRouteLFU()),
//    LEAST_RECENTLY_USED("least_recently_used", (byte) 7, "最近最久未使用", new ExecutorRouteLRU()),

    /**
     * 故障转移
     */
    FAIL_OVER("fail_over", "故障转移"),

    /**
     * 忙碌转移
     */
    BUSY_OVER("busy_over", "忙碌转移"),

    /**
     * 广播
     */
    BROADCAST("broadcast", "广播"),
    ;

    private final String name;
    private final String desc;

    ExecRouteStrategyEnum(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

}
