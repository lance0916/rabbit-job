package com.snail.job.client.thread;

import com.snail.job.client.biz.AdminBiz;
import com.snail.job.common.thread.RabbitJobAbstractThread;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.snail.job.common.constant.CommonConstants.BEAT_TIME;

/**
 * 发送任务执行结果给调度中心
 * @author 吴庆龙
 * @date 2020/5/26 4:43 下午
 */
@Component
public class RegisterThread extends RabbitJobAbstractThread {

    @Resource
    private AdminBiz adminBiz;

    @Override
    protected void doRun() throws InterruptedException {
        long startMillis = System.currentTimeMillis();
        adminBiz.register();
        long costMillis = System.currentTimeMillis() - startMillis;
        if (running) {
            Thread.sleep(BEAT_TIME - costMillis);
        }
    }

    @Override
    protected void destroy() {
        adminBiz.remove();
    }

    @Override
    protected String getThreadName() {
        return "RegisterThread";
    }

}
