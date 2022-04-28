package com.snail.job.common.proxy;

import com.snail.job.common.model.CallbackParam;
import com.snail.job.common.model.RegistryParam;
import com.snail.job.common.model.ResultT;
import com.snail.job.common.tools.HttpTool;

import java.util.List;

import static com.snail.job.common.constant.CommonConstants.URL_SEPARATOR;

/**
 * @author 吴庆龙
 */
public class AdminProxy {

    /**
     * admin 地址
     */
    private final String address;

    /**
     * 客户端的key和密钥
     * 用于消息加密
     */
    private final String secretKey;

    public AdminProxy(String address, String secretKey) {
        // 追加 / 符号
        if (!address.endsWith(URL_SEPARATOR)) {
            address = address + URL_SEPARATOR;
        }

        this.address = address;
        this.secretKey = secretKey;
    }

    public ResultT<String> beat() {
        return HttpTool.post(address + "api/beat", null, secretKey);
    }

    public ResultT<String> registry(RegistryParam param) {
        return HttpTool.post(address + "api/registry", param, secretKey);
    }

    public ResultT<String> remove(RegistryParam param) {
        return HttpTool.post(address + "api/remove", param, secretKey);
    }

    public ResultT<String> callback(List<CallbackParam> list) {
        return HttpTool.post(address + "api/callback", list, secretKey);
    }
}
