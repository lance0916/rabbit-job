package com.snail.job.admin.route;

/**
 * 任务路由策略
 * @author 吴庆龙
 * @date 2020/6/4 11:23 上午
 */
public enum RouteStrategyEnum {

    /**
     * 第一个
     */
    FIRST("first", "第一个"),

    /**
     * 最后一个
     */
    LAST("last", "最后一个"),

    /**
     * 一致性hash
     */
    CONSISTENT_HASH("consistent_hash", "一致性HASH"),

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

    RouteStrategyEnum(String name, String desc) {
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
