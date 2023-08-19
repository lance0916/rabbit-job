package com.example.admin.route.strategy;

import com.example.admin.service.JobExecutorService;
import com.example.admin.route.AbstractRoute;
import com.example.common.model.ResultT;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * 故障转移
 * @author WuQinglong
 */
@Component
public class FailOverRoute extends AbstractRoute {

    @Resource
    private JobExecutorService jobExecutorService;

    @Override
    public String getExecutorAddress(Long jobId, List<String> addresses) {
        for (String address : addresses) {
            ResultT<String> result;
            try {
                result = jobExecutorService.beat(address);
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
