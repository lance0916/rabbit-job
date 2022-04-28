package com.snail.job.common.constant;

/**
 * @author 吴庆龙
 */
public interface CommonConstants {

    /**
     * URL 中的分隔符
     */
    String URL_SEPARATOR = "/";

    /**
     * 执行器注册间隔时间
     */
    int BEAT_TIME = 30 * 1000;

    /**
     * 执行器心跳死亡时间
     */
    int BEAT_TIME_OUT = 3 * BEAT_TIME;
}
