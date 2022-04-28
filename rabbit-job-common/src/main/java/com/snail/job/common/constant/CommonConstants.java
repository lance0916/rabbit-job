package com.snail.job.common.constant;

/**
 * @author WuQinglong
 */
public interface CommonConstants {

    /**
     * URL 中的分隔符
     */
    String URL_SEPARATOR = "/";

    /**
     * 执行器注册间隔时间
     */
    int REGISTER_INTERVAL_TIME = 10 * 1000;

    /**
     * 执行器心跳死亡时间
     */
    int EXECUTOR_TIME_OUT = 3 * REGISTER_INTERVAL_TIME / 1000;
}
