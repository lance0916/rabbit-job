package com.snail.job.admin.route.strategy;

import com.snail.job.admin.route.AbstractRoute;
import org.springframework.stereotype.Component;

/**
 * 最后一个执行
 * @author 吴庆龙
 * @date 2020/6/17 10:11 上午
 */
@Component
public class LastRoute extends AbstractRoute {

    @Override
    public String getExecutorAddress(Long appId, Long jobId, String[] addresses) {
        if (addresses.length == 0) {
            return null;
        }
        return addresses[addresses.length - 1];
    }

}
