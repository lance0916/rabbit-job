package com.snail.job.admin.route.strategy;

import com.snail.job.admin.biz.JobExecutorBiz;
import com.snail.job.admin.route.AbstractRoute;
import com.snail.job.common.model.IdleBeatParam;
import com.snail.job.common.model.ResultT;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * 忙碌转移
 * @author WuQinglong
 */
@Component
public class BusyOverRoute extends AbstractRoute {

    @Resource
    private JobExecutorBiz jobExecutorBiz;

    @Override
    public String getExecutorAddress(Long jobId, List<String> addresses) {
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
