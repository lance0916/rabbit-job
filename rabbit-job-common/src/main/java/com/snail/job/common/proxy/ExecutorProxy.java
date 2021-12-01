package com.snail.job.common.proxy;

import com.snail.job.common.model.IdleBeatParam;
import com.snail.job.common.model.ResultT;
import com.snail.job.common.model.TriggerParam;
import com.snail.job.common.tools.HttpTool;

import static com.snail.job.common.constant.CommonConstants.URL_SEPARATOR;

/**
 * @author 吴庆龙
 * @date 2020/6/18 11:46 上午
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
        return HttpTool.post(address + "beat", null, secretKey);
    }

    public ResultT<String> idleBeat(IdleBeatParam idleBeatParam) {
        return HttpTool.post(address + "ideaBeat", idleBeatParam, secretKey);
    }

    public ResultT<String> run(TriggerParam triggerParam) {
        return HttpTool.post(address + "run", triggerParam, secretKey);
    }
}
