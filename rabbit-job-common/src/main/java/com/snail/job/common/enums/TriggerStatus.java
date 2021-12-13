package com.snail.job.common.enums;

/**
 * 任务调度状态
 * @author 吴庆龙
 * @date 2020/7/10 10:23 上午
 */
public enum TriggerStatus {

    /**
     * 已停止
     */
    STOPPED("stopped", 0, "已停止"),

    /**
     * 运行中
     */
    RUNNING("running", 1, "运行中"),
    ;

    private final String name;
    private final Integer value;
    private final String desc;

    TriggerStatus(String name, Integer value, String desc) {
        this.name = name;
        this.value = value;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public Integer getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
}
