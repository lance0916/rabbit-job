package com.example.common.proxy;

import com.example.common.model.RegistryParam;
import com.example.common.tools.HttpTool;
import com.example.common.model.CallbackParam;
import com.example.common.model.ResultT;

import java.util.List;

import static com.example.common.constant.CommonConstants.URL_SEPARATOR;

/**
 * @author WuQinglong
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
