package com.snail.job.client.biz;

import com.snail.job.common.model.CallbackParam;
import com.snail.job.common.model.RegistryParam;
import com.snail.job.common.model.ResultT;
import com.snail.job.common.proxy.AdminProxy;
import com.snail.job.client.config.JobClientProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 向调度中心注册执行器节点
 * TODO 优化 admin 的选择
 *
 * @author 吴庆龙
 * @date 2020/5/25 4:00 下午
 */
@Component
public class AdminBiz {
    private final Logger log = LoggerFactory.getLogger(AdminBiz.class);

    /**
     * 配置文件
     */
    @Resource
    private JobClientProperties jobClientProperties;

    /**
     * 调度中心
     */
    @Resource
    private List<AdminProxy> adminProxies;

    /**
     * 注册
     */
    public void register() {
        // FIXME 支持配置 ip
        RegistryParam param = new RegistryParam(
                jobClientProperties.getExecutor().getAppName(),
                jobClientProperties.getExecutor().getAddress()
        );

        AdminProxy proxy = adminProxies.get(0);
        ResultT<String> result = proxy.registry(param);
        if (ResultT.SUCCESS_CODE != result.getCode()) {
            log.error("注册执行器失败。原因：{}", result.getMsg());
        }
    }

    /**
     * 移除
     */
    public void remove() {
        // FIXME 支持配置 ip
        RegistryParam param = new RegistryParam(
                jobClientProperties.getExecutor().getAppName(),
                jobClientProperties.getExecutor().getAddress()
        );

        AdminProxy proxy = adminProxies.get(0);
        ResultT<String> result = proxy.remove(param);
        if (ResultT.SUCCESS_CODE != result.getCode()) {
            log.error("注销执行器失败。原因：{}", result.getMsg());
        }
    }

    /**
     * 回调任务执行结果
     */
    public void callback(List<CallbackParam> callbackParamList) {
        AdminProxy proxy = adminProxies.get(0);
        ResultT<String> result = proxy.callback(callbackParamList);
        if (ResultT.SUCCESS_CODE != result.getCode()) {
            log.error("回调返回失败。原因：{}", result.getMsg());
        }
    }

}
