package com.example.common.enums;

/**
 * 告警状态
 * @author WuQinglong
 */
public enum AlarmStatus {

    /**
     * 无需告警
     */
    NO_ALARM("no_alarm", 0, "无需告警"),

    /**
     * 待告警
     */
    WAIT_ALARM("wait_alarm", 1, "待告警"),

    /**
     * 已告警
     */
    ALARM_FINISH("alarm_finish", 2, "已告警"),
    ;

    private final String name;
    private final Integer value;
    private final String desc;

    AlarmStatus(String name, Integer value, String desc) {
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
