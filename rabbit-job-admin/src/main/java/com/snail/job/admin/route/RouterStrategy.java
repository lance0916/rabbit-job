package com.snail.job.admin.route;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 任务路由
 * @author 吴庆龙
 * @date 2020/6/4 11:23 上午
 */
public abstract class RouterStrategy {
    protected final Logger log = LoggerFactory.getLogger(RouterStrategy.class);

    /**
     * route address
     * @param jobId     触发参数
     * @param addresses 地址列表
     * @return 地址
     */
    public abstract String route(Long jobId, String[] addresses);

}
