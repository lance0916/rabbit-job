package com.snail.job.admin;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.snail.job.admin.model.Executor;
import com.snail.job.admin.service.AppService;
import com.snail.job.admin.service.ExecutorService;
import com.snail.job.admin.service.JobInfoService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static com.snail.job.common.constant.CommonConstants.EXECUTOR_TIME_OUT;

/**
 * @author WuQinglong created on 2021/12/18 14:57
 */
@SpringBootTest
public class MyBatisPlusTest {

    @Resource
    private AppService appService;
    @Resource
    private ExecutorService executorService;
    @Resource
    private JobInfoService jobInfoService;

    @Test
    public void testUpdate() {
        Executor executor = executorService.getById(1);
        System.out.println(JSONUtil.toJsonStr(executor));

        executor.setDeleted(0)
                .setUpdateTime(new Date());
        executorService.saveOrUpdate(executor);
    }

    @Test
    public void test() {
        long startMillis = System.currentTimeMillis();

        // 获取所有的执行器
        List<Executor> executors = executorService.list(Wrappers.<Executor>query().eq(Executor.DELETED, 0));
        System.out.println(executors);

        // 判断执行器的更新时间，是否超过了三个注册间隔时间
        Date timeOutDate = new Date(startMillis - EXECUTOR_TIME_OUT);

        // 过期的执行器集合
        List<Executor> invalidExecutors = new ArrayList<>();
        List<Executor> validExecutors = new ArrayList<>();
        for (Executor executor : executors) {
            Date updateTime = executor.getUpdateTime();
            if (timeOutDate.after(updateTime)) {
                executor.setDeleted(1);
                invalidExecutors.add(executor);
            } else {
                validExecutors.add(executor);
            }
        }
        System.out.println(invalidExecutors);
        System.out.println(validExecutors);

    }

}
