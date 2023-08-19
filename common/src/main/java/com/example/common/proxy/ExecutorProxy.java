package com.example.common.proxy;

import com.example.common.tools.HttpTool;
import com.example.common.model.IdleBeatParam;
import com.example.common.model.ResultT;
import com.example.common.model.TriggerParam;

import static com.example.common.constant.CommonConstants.URL_SEPARATOR;

/**
 * @author WuQinglong
 */
public class ExecutorProxy {

    /**
     * 执行器地址
     */
    private final String address;

    /**
     * 服务器的key和密钥
     * 用于消息加密
     */
    private final String secretKey;

    public ExecutorProxy(String address, String secretKey) {
        if (!address.endsWith(URL_SEPARATOR)) {
            address = address + URL_SEPARATOR;
        }
        this.address = address;
        this.secretKey = secretKey;
    }

    public ResultT<String> beat() {
        return HttpTool.post(address + "rabbit-job/beat", null, secretKey);
    }

    public ResultT<String> idleBeat(IdleBeatParam idleBeatParam) {
        return HttpTool.post(address + "rabbit-job/ideaBeat", idleBeatParam, secretKey);
    }

    public ResultT<String> run(TriggerParam triggerParam) {
        return HttpTool.post(address + "rabbit-job/run", triggerParam, secretKey);
    }
}
