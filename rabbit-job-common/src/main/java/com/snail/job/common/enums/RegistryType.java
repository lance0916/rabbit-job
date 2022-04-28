package com.snail.job.common.enums;

/**
 * 执行器注册类型
 * @author 吴庆龙
 */
public enum RegistryType {

    /**
     * 自动注册
     */
    AUTO("auto", (byte) 0, "自动注册"),

    /**
     * 手动注册
     */
    MANUAL("manual", (byte) 1, "手动注册"),
    ;

    private final String name;
    private final Byte value;
    private final String desc;

    RegistryType(String name, Byte value, String desc) {
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
