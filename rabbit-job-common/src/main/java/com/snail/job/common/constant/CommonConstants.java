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
    int REGISTER_INTERVAL_TIME = 1000 * 10;

    /**
     * 执行器心跳死亡时间
     */
    int EXECUTOR_TIME_OUT = REGISTER_INTERVAL_TIME * 3;
}
