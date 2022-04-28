package com.snail.job.admin.route.strategy;

import com.snail.job.admin.biz.JobExecutorBiz;
import com.snail.job.admin.route.AbstractRoute;
import com.snail.job.common.model.ResultT;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 故障转移
 * @author 吴庆龙
 */
@Component
public class FailOverRoute extends AbstractRoute {

    @Resource
    private JobExecutorBiz jobExecutorBiz;

    @Override
    public String getExecutorAddress(Long appId, Long jobId, String[] addresses) {
        for (String address : addresses) {
            ResultT<String> result;
            try {
                result = jobExecutorBiz.beat(address);
            } catch (Exception e) {
                log.error("选择执行器。beat接口异常，执行器：{}，原因：{}", address, e);
                continue;
            }

            if (result.getCode() == ResultT.SUCCESS_CODE) {
                return address;
            }
        }
        return null;
    }

}
