package com.example.client.thread;

import com.example.client.biz.AdminBiz;
import com.example.common.RabbitJobAbstractThread;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.example.common.constant.CommonConstants.REGISTER_INTERVAL_TIME;

/**
 * 注册客户端线程
 * @author WuQinglong
 */
@Component
public class RegisterThread extends RabbitJobAbstractThread {

    @Resource
    private AdminBiz adminBiz;

    @Override
    protected void execute() throws InterruptedException {
        long startMillis = System.currentTimeMillis();
        adminBiz.register();
        long costMillis = System.currentTimeMillis() - startMillis;
        if (running) {
            Thread.sleep(REGISTER_INTERVAL_TIME - costMillis);
        }
    }

    @Override
    protected void after() {
        adminBiz.remove();
    }

    @Override
    protected String getThreadName() {
        return "RegisterThread";
    }

}
