package com.snail.job.common.constant;

/**
 * 服务状态标识
 * @author WuQinglong
 * @since 2021/12/24 10:01 上午
 */
public class ServiceStatus {

    public static Status status = Status.RUNNING;

    /**
     * 服务状态
     */
    public static enum Status {
        RUNNING, STOPPING;
    }

}
