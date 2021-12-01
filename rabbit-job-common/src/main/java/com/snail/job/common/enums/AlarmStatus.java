package com.snail.job.common.enums;

/**
 * 告警状态
 * @author 吴庆龙
 * @date 2020/6/30 2:31 下午
 */
public enum AlarmStatus {

    /**
     * 无需告警
     */
    NO_ALARM("no_alarm", (byte) 0, "无需告警"),

    /**
     * 待告警
     */
    WAIT_ALARM("wait_alarm", (byte) 1, "待告警"),

    /**
     * 已告警
     */
    ALARM_FINISH("alarm_finish", (byte) 2, "已告警"),
    ;

    private final String name;
    private final Byte value;
    private final String desc;

    AlarmStatus(String name, Byte value, String desc) {
        this.name = name;
        this.value = value;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public Byte getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
}
