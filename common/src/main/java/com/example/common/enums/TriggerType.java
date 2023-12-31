package com.example.common.enums;

/**
 * 任务触发类型
 * @author WuQinglong
 */
public enum TriggerType {

    /**
     * 手动触发
     */
    MANUAL("manual", "手动触发"),

    /**
     * Cron触发
     */
    CRON("cron", "Cron触发"),

    /**
     * 失败重试触发
     */
    RETRY("retry", "失败重试触发"),

    /**
     * API触发
     */
    API("api", "API触发"),
    ;

    private final String name;
    private final String desc;

    TriggerType(String name, String desc) {
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
