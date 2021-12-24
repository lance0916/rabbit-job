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
 * 与调度中心进行通信
 * 采用轮训算法
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
     * 上次使用的调度中心下标
     */
    private static int lastIndex = 0;

    /**
     * 获取调度中心proxy实例
     */
    public AdminProxy getAdminProxy() {
        // 回到 0 处，形成循环
        if (lastIndex >= adminProxies.size()) {
            lastIndex = 0;
        }
        return adminProxies.get(lastIndex++);
    }

    /**
     * 注册
     */
    public void register() {
        // FIXME 支持配置 ip
        RegistryParam param = new RegistryParam(
                jobClientProperties.getExecutor().getAppName(),
                jobClientProperties.getExecutor().getAddress()
        );

        AdminProxy proxy = getAdminProxy();
        ResultT<String> result = proxy.registry(param);
        if (ResultT.NETWORK_ERROR == result.getCode()) {
            // 切换别的调度中心重试
            for (int i = 0; i < 3; i++) {
                proxy = getAdminProxy();
                result = proxy.registry(param);
                if (ResultT.SUCCESS_CODE == result.getCode()) {
                    break;
                }
            }
        }
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

        AdminProxy proxy = getAdminProxy();
        ResultT<String> result = proxy.remove(param);
        if (ResultT.NETWORK_ERROR == result.getCode()) {
            // 切换别的调度中心重试
            for (int i = 0; i < 3; i++) {
                proxy = getAdminProxy();
                result = proxy.remove(param);
                if (ResultT.SUCCESS_CODE == result.getCode()) {
                    break;
                }
            }
        }
        if (ResultT.SUCCESS_CODE != result.getCode()) {
            log.error("注销执行器失败。原因：{}", result.getMsg());
        }
    }

    /**
     * 回调任务执行结果
     */
    public void callback(List<CallbackParam> callbackParams) {
        AdminProxy proxy = getAdminProxy();
        ResultT<String> result = proxy.callback(callbackParams);
        if (ResultT.NETWORK_ERROR == result.getCode()) {
            // 切换别的调度中心重试
            for (int i = 0; i < 3; i++) {
                proxy = getAdminProxy();
                result = proxy.callback(callbackParams);
                if (ResultT.SUCCESS_CODE == result.getCode()) {
                    break;
                }
            }
        }
        if (ResultT.SUCCESS_CODE != result.getCode()) {
            log.error("回调返回失败。原因：{}", result.getMsg());
        }
    }

}
