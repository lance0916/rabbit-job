package com.snail.job.admin.route.strategy;

import com.snail.job.admin.route.AbstractRoute;
import org.springframework.stereotype.Component;

/**
 * 第一个执行
 * @author WuQinglong
 */
@Component
public class FirstRoute extends AbstractRoute {

    @Override
    public String getExecutorAddress(Long appId, Long jobId, String[] addresses) {
        if (addresses.length == 0) {
            return null;
        }
        return addresses[0];
    }

}
