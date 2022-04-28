package com.snail.job.common.constant;

/**
 * 服务状态标识
 * @author WuQinglong
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
