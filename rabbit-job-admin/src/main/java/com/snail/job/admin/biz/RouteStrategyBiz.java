package com.snail.job.admin.biz;

import com.snail.job.admin.route.ClientRouter;
import com.snail.job.admin.route.strategy.*;
import com.snail.job.common.exception.SnailJobException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.snail.job.admin.route.RouteStrategyEnum.*;

/**
 * @author WuQinglong
 * @date 2021/9/7 12:08 下午
 */
@Component
public class RouteStrategyBiz {

    @Resource
    private BusyOverRoute busyOverRoute;
    @Resource
    private FailOverRoute failOverRoute;
    @Resource
    private FirstRoute firstRoute;
    @Resource
    private LastRoute lastRoute;
    @Resource
    private ConsistentHashRoute consistentHashRoute;

    /**
     * 获取匹配的路由策略
     */
    public ClientRouter match(String strategy) {
        if (FIRST.getName().equals(strategy)) {
            return firstRoute;
        }
        if (LAST.getName().equals(strategy)) {
            return lastRoute;
        }
        if (BUSY_OVER.getName().equals(strategy)) {
            return busyOverRoute;
        }
        if (FAIL_OVER.getName().equals(strategy)) {
            return failOverRoute;
        }
        if (CONSISTENT_HASH.getName().equals(strategy)) {
            return consistentHashRoute;
        }
        throw new SnailJobException("未配置路由策略！！！");
    }

}
