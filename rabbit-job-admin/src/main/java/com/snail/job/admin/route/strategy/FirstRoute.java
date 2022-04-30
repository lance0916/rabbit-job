package com.snail.job.admin.route.strategy;

import com.snail.job.admin.route.AbstractRoute;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * 第一个执行
 * @author WuQinglong
 */
@Component
public class FirstRoute extends AbstractRoute {

    @Override
    public String getExecutorAddress(Long jobId, List<String> addresses) {
        if (addresses.size() == 0) {
            return null;
        }
        return addresses.get(0);
    }

}
