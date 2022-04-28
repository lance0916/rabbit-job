package com.snail.job.admin.route;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 任务路由
 * @author WuQinglong
 */
public abstract class AbstractRoute {
    protected final Logger log = LoggerFactory.getLogger(AbstractRoute.class);

    /**
     * route address
     *
     * @param appId
     * @param jobId     触发参数
     * @param addresses 地址列表
     * @return 地址
     */
    public abstract String getExecutorAddress(Long appId, Long jobId, String[] addresses);

}
