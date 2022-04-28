package com.snail.job.client.biz;

import com.snail.job.common.exception.RabbitJobException;
import com.snail.job.common.model.CallbackParam;
import com.snail.job.common.model.RegistryParam;
import com.snail.job.common.model.ResultT;
import com.snail.job.common.proxy.AdminProxy;
import com.snail.job.client.config.RabbitJobProperties;
import com.snail.job.common.tools.StrTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 与调度中心进行通信
 * 采用轮训算法
 *
 * @author WuQinglong
 */
@Component
public class AdminBiz {
    private final Logger log = LoggerFactory.getLogger(AdminBiz.class);

    /**
     * 服务器端口
     */
    @Value("${server.port:8080}")
    private int serverPort;

    /**
     * 配置文件
     */
    @Resource
    private RabbitJobProperties rabbitJobProperties;

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
        RegistryParam param = getRegistryParam();

        AdminProxy proxy = getAdminProxy();
        ResultT<String> result = proxy.registry(param);
        if (ResultT.NETWORK_ERROR == result.getCode() || ResultT.SERVICE_DOWN == result.getCode()) {
            log.error("注册执行器失败，进行重试。原因：{}", result.getMsg());
            // 切换别的调度中心重试，最多重试 3 次
            for (int i = 0; i < 3; i++) {
                proxy = getAdminProxy();
                result = proxy.registry(param);
                if (ResultT.SUCCESS_CODE == result.getCode()) {
                    break;
                } else {
                    log.error("注册执行器失败。重试:{}次，原因：{}", i, result.getMsg());
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
        RegistryParam param = getRegistryParam();

        AdminProxy proxy = getAdminProxy();
        ResultT<String> result = proxy.remove(param);
        if (ResultT.NETWORK_ERROR == result.getCode() || ResultT.SERVICE_DOWN == result.getCode()) {
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
     * 获取注册参数
     */
    private RegistryParam getRegistryParam() {
        RabbitJobProperties.Executor executor = rabbitJobProperties.getExecutor();
        String address = executor.getAddress();

        // 优先使用配置的地址
        if (StrTool.isEmpty(address)) {
            String ip = executor.getIp();
            if (StrTool.isEmpty(ip)) {
                throw new RabbitJobException("address和ip必须配置一个");
            }
            address = "http://" + ip + ":" + serverPort;
        }

        return new RegistryParam(executor.getAppName(), address);
    }

    /**
     * 回调任务执行结果
     */
    public void callback(List<CallbackParam> callbackParams) {
        AdminProxy proxy = getAdminProxy();
        ResultT<String> result = proxy.callback(callbackParams);
        if (ResultT.NETWORK_ERROR == result.getCode() || ResultT.SERVICE_DOWN == result.getCode()) {
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
