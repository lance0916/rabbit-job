package com.snail.job.admin.thread;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.snail.job.admin.model.Executor;
import com.snail.job.admin.service.ExecutorService;
import com.snail.job.common.thread.RabbitJobAbstractThread;
import com.snail.job.common.tools.GsonTool;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;
import static com.snail.job.common.constant.CommonConstants.EXECUTOR_TIME_OUT;

/**
 * 清理无效的执行器
 * @author WuQinglong
 */
@Component
public class ExecutorSweepThread extends RabbitJobAbstractThread {

    @Resource
    private ExecutorService executorService;

    @Override
    public void execute() throws InterruptedException {
        long startMillis = System.currentTimeMillis();

        // 获取所有的执行器
        List<Executor> executors = executorService.list(Wrappers.<Executor>query().eq(Executor.DELETED, 0));

        // 判断执行器的更新时间，是否超过了三个注册间隔时间
        Date timeOutDate = new Date(startMillis - EXECUTOR_TIME_OUT);

        // 过期的执行器集合
        List<Executor> invalidExecutors = new ArrayList<>();
        for (Executor executor : executors) {
            Date updateTime = executor.getUpdateTime();
            if (timeOutDate.after(updateTime)) {
                executor.setDeleted(1);
                invalidExecutors.add(executor);
            }
        }

        // 批量软删
        executorService.saveOrUpdateBatch(invalidExecutors);
        log.info("清理执行器:{}", GsonTool.toJson(invalidExecutors));

        // 休眠
        long costMillis = System.currentTimeMillis() - startMillis;
        if (running && costMillis < EXECUTOR_TIME_OUT) {
            // 30 秒 - 耗时，每次扫描间隔差不多为 30 秒
            Thread.sleep(EXECUTOR_TIME_OUT - costMillis);
        }
    }

    @Override
    public String getThreadName() {
        return this.getClass().getSimpleName();
    }

}
