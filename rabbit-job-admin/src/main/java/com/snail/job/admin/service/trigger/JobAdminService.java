package com.snail.job.admin.service.trigger;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.snail.job.admin.model.Executor;
import com.snail.job.admin.model.JobLog;
import com.snail.job.admin.service.ExecutorService;
import com.snail.job.admin.service.JobLogService;
import com.snail.job.common.model.CallbackParam;
import com.snail.job.common.model.RegistryParam;
import com.snail.job.common.model.ResultT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author 吴庆龙
 * @date 2020/6/3 11:34 上午
 */
@Slf4j
@Component
public class JobAdminService {

    @Resource(name = "apiThreadPool")
    private ThreadPoolExecutor apiThreadPool;
    @Resource
    private ExecutorService executorService;
    @Resource
    private JobLogService jobLogService;

    /**
     * 心跳监测
     */
    public ResultT<String> beat() {
        return ResultT.SUCCESS;
    }

    /**
     * 注册
     */
    public ResultT<String> registry(RegistryParam param) {
        String address = param.getAddress();
        String appName = param.getAppName();
        if (StrUtil.isBlank(address) || StrUtil.isBlank(appName)) {
            return new ResultT<>(ResultT.FAIL_CODE, "参数缺失");
        }

        // 查询是否存在
        QueryWrapper<Executor> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("app_name", appName).eq("address", address);
        Executor dbExecutor = executorService.getOne(queryWrapper);
        Executor executor;
        if (dbExecutor == null) {
            // 新增
            executor = new Executor()
                    .setAppName(appName)
                    .setAddress(address)
                    .setCreateTime(LocalDateTime.now());
        } else {
            // 更新
            executor = new Executor()
                    .setId(dbExecutor.getId());
        }
        executor.setDeleted(0)
                .setUpdateTime(LocalDateTime.now());

        executorService.saveOrUpdate(executor);
        return ResultT.SUCCESS;
    }

    /**
     * 移除，执行软删
     */
    public ResultT<String> remove(RegistryParam param) {
        String appName = param.getAppName();
        String address = param.getAddress();
        if (StrUtil.isBlank(appName) || StrUtil.isBlank(address)) {
            return new ResultT<>(ResultT.FAIL_CODE, "参数缺失");
        }

        // 查询是否存在
        QueryWrapper<Executor> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("app_name", appName).eq("address", address);
        Executor dbExecutor = executorService.getOne(queryWrapper);
        if (dbExecutor == null) {
            return new ResultT<>(ResultT.FAIL_CODE, "执行器不存在");
        }

        // 从 executor 表中移除，并从执行地址中移除
        Executor executor = new Executor()
                .setId(dbExecutor.getId())
                .setDeleted(1)
                .setUpdateTime(LocalDateTime.now());
        executorService.updateById(executor);
        return ResultT.SUCCESS;
    }

    /**
     * 接收任务执行结果
     */
    public ResultT<String> callback(List<CallbackParam> list) {
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
