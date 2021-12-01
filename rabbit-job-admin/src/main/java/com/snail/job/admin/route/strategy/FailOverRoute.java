package com.snail.job.admin.route.strategy;

import com.snail.job.admin.biz.JobExecutorBiz;
import com.snail.job.admin.route.ClientRouter;
import com.snail.job.common.model.ResultT;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 故障转移
 * @author 吴庆龙
 * @date 2020/6/17 10:11 上午
 */
@Component
public class FailOverRoute extends ClientRouter {

    @Resource
    private JobExecutorBiz jobExecutorBiz;

    @Override
    public String route(Long jobId, String[] addresses) {
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
