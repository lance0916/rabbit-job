package com.snail.job.admin.route.strategy;

import com.snail.job.admin.biz.JobExecutorBiz;
import com.snail.job.admin.route.ExecRouter;
import com.snail.job.common.model.IdleBeatParam;
import com.snail.job.common.model.ResultT;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 忙碌转移
 * @author 吴庆龙
 * @date 2020/6/17 10:11 上午
 */
@Component
public class ExecRouteBusyOver extends ExecRouter {

    @Resource
    private JobExecutorBiz jobExecutorBiz;

    @Override
    public String route(Long jobId, String[] addresses) {
        for (String address : addresses) {
            IdleBeatParam idleBeatParam = new IdleBeatParam(jobId);

            ResultT<String> result;
            try {
                result = jobExecutorBiz.idleBeat(address, idleBeatParam);
            } catch (Exception e) {
                log.error("选择执行器。idleBeat接口异常，执行器：{}，原因：{}", address, e);
                continue;
            }

            if (result.getCode() == ResultT.SUCCESS_CODE) {
                return address;
            } else {
                log.info("选择执行器。执行器：[{}]忙碌，继续寻找执行器...", address);
            }
        }
        return null;
    }
}
