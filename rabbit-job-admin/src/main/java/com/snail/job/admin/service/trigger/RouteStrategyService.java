package com.snail.job.admin.service.trigger;

import com.snail.job.admin.route.strategy.ExecRouteBusyOver;
import com.snail.job.admin.route.ExecRouter;
import com.snail.job.admin.route.strategy.ExecRouteFailOver;
import com.snail.job.admin.route.strategy.ExecRouteFirst;
import com.snail.job.admin.route.strategy.ExecRouteLast;
import com.snail.job.common.exception.SnailJobException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.snail.job.admin.route.ExecRouteStrategyEnum.*;

/**
 * @author WuQinglong
 * @date 2021/9/7 12:08 下午
 */
@Component
public class RouteStrategyService {

    @Resource
    private ExecRouteBusyOver execRouteBusyOver;
    @Resource
    private ExecRouteFailOver execRouteFailOver;
    @Resource
    private ExecRouteFirst execRouteFirst;
    @Resource
    private ExecRouteLast execRouteLast;

    /**
     * 获取匹配的路由策略
     */
    public ExecRouter match(String strategy) {
        if (FIRST.getName().equals(strategy)) {
            return execRouteFirst;
        }
        if (LAST.getName().equals(strategy)) {
            return execRouteLast;
        }
        if (BUSY_OVER.getName().equals(strategy)) {
            return execRouteBusyOver;
        }
        if (FAIL_OVER.getName().equals(strategy)) {
            return execRouteFailOver;
        }
        throw new SnailJobException("未配置路由策略！！！");
    }

}
