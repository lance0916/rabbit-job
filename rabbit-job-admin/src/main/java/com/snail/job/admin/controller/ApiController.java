package com.snail.job.admin.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.snail.job.admin.model.Executor;
import com.snail.job.admin.model.JobLog;
import com.snail.job.admin.service.ExecutorService;
import com.snail.job.admin.service.JobLogService;
import com.snail.job.common.annotation.CheckSign;
import com.snail.job.common.constant.ServiceStatus;
import com.snail.job.common.model.CallbackParam;
import com.snail.job.common.model.RegistryParam;
import com.snail.job.common.model.ResultT;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 接收 Worker 节点的请求
 * @author WuQinglong
 */
@Slf4j
@RestController
@RequestMapping("/api")
@CheckSign
public class ApiController {

    @Resource(name = "apiThreadPool")
    private ThreadPoolExecutor apiThreadPool;
    @Resource
    private ExecutorService executorService;
    @Resource
    private JobLogService jobLogService;

    /**
     * 心跳
     */
    @PostMapping("/beat")
    public ResultT<String> beat() {
        if (ServiceStatus.status == ServiceStatus.Status.STOPPING) {
            return ResultT.DOWN;
        }
        return ResultT.SUCCESS;
    }

    /**
     * 注册执行器
     */
    @PostMapping("/registry")
    public ResultT<String> registry(@RequestBody RegistryParam param) {
        String address = param.getAddress();
        String appName = param.getAppName();
        if (StrUtil.isBlank(address) || StrUtil.isBlank(appName)) {
            return new ResultT<>(ResultT.FAIL_CODE, "参数缺失");
        }

        apiThreadPool.submit(() -> {
            // 查询是否存在
            Executor executor = executorService.getOne(Wrappers.<Executor>query()
                    .eq(Executor.APP_NAME, appName)
                    .eq(Executor.ADDRESS, address)
            );
            if (executor == null) {
                executor = new Executor()
                        .setAppName(appName)
                        .setAddress(address)
                        .setCreateTime(LocalDateTime.now());
            } else {
                executor.setDeleted(0)
                        .setUpdateTime(LocalDateTime.now());
            }
            executorService.saveOrUpdate(executor);
        });
        log.info("执行器注册成功。{}", param);
        return ResultT.SUCCESS;
    }

    /**
     * 移除执行器
     */
    @PostMapping("/remove")
    public ResultT<String> remove(@RequestBody RegistryParam param) {
        String appName = param.getAppName();
        String address = param.getAddress();
        if (StrUtil.isBlank(appName) || StrUtil.isBlank(address)) {
            return new ResultT<>(ResultT.FAIL_CODE, "参数缺失");
        }

        // 查询是否存在
        Executor executor = executorService.getOne(Wrappers.<Executor>query()
                .eq(Executor.APP_NAME, appName)
                .eq(Executor.ADDRESS, address)
        );
        if (executor == null) {
            return new ResultT<>(ResultT.FAIL_CODE, "执行器不存在");
        }

        apiThreadPool.submit(() -> {
            // 从 executor 表中移除，并从执行地址中移除
            executor.setDeleted(1)
                    .setUpdateTime(LocalDateTime.now());
            executorService.updateById(executor);
        });
        log.info("执行器注销成功。{}", param);
        return ResultT.SUCCESS;
    }

    /**
     * 任务回调
     */
    @PostMapping("/callback")
    public ResultT<String> callback(@RequestBody List<CallbackParam> list) {
        for (CallbackParam param : list) {
            Long logId = param.getLogId();
            Integer execCode = param.getExecCode();
            if (logId == null || execCode == null) {
                return new ResultT<>(ResultT.FAIL_CODE, "参数缺失");
            }
        }
        apiThreadPool.submit(() -> {
            for (CallbackParam param : list) {
                Long logId = param.getLogId();

                // 查询任务日志
                JobLog jobLog = jobLogService.getById(logId);
                if (jobLog == null) {
                    log.error("任务不存在，logId:{}", logId);
                    continue;
                }
                if (jobLog.getExecCode() != 0) {
                    log.error("任务已回调，logId:{}", logId);
                    continue;
                }

                LocalDateTime beginExecTime = param.getBeginExecTime();
                LocalDateTime endExecTime = param.getEndExecTime();

                // 更新 JobLog 执行结果
                JobLog updateLog = new JobLog()
                        .setId(logId)
                        .setExecCostTime(param.getExecCode())
                        .setExecMsg(param.getExecMsg())
                        .setExecBeginTime(param.getBeginExecTime())
                        .setExecEndTime(param.getEndExecTime());
                if (beginExecTime != null && endExecTime != null) {
                    Duration duration = Duration.between(beginExecTime, endExecTime);
                    updateLog.setExecCostTime((int) duration.toMillis());
                }
                jobLogService.updateById(updateLog);
            }
        });
        return ResultT.SUCCESS;
    }

}
