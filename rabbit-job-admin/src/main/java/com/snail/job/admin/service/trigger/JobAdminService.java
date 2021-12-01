package com.snail.job.admin.service.trigger;

import cn.hutool.core.util.StrUtil;
import com.snail.job.admin.entity.Executor;
import com.snail.job.admin.entity.JobLog;
import com.snail.job.admin.repository.ExecutorRepository;
import com.snail.job.admin.repository.JobLogRepository;
import com.snail.job.common.model.CallbackParam;
import com.snail.job.common.model.RegistryParam;
import com.snail.job.common.model.ResultT;
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
@Component
public class JobAdminService {

    @Resource(name = "apiThreadPool")
    private ThreadPoolExecutor apiThreadPool;
    @Resource
    private ExecutorRepository executorRepository;
    @Resource
    private JobLogRepository jobLogRepository;

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
        Executor dbExecutor = executorRepository.findFirstByAppNameEqualsAndAddressEquals(appName, address);
        Executor updateExecutor;
        if (dbExecutor == null) {
            // 新增
            updateExecutor = Executor.builder()
                    .appName(appName)
                    .address(address)
                    .deleted((byte) 0)
                    .createTime(LocalDateTime.now())
                    .updateTime(LocalDateTime.now())
                    .build();
        } else {
            // 更新
            updateExecutor = Executor.builder()
                    .id(dbExecutor.getId())
                    .deleted((byte) 0)
                    .updateTime(LocalDateTime.now())
                    .build();
        }
        executorRepository.saveAndFlush(updateExecutor);

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
        Executor dbExecutor = executorRepository.findFirstByAppNameEqualsAndAddressEquals(appName, address);
        if (dbExecutor == null) {
            return new ResultT<>(ResultT.FAIL_CODE, "执行器不存在");
        }

        // 从 executor 表中移除，并从执行地址中移除
        Executor executor = Executor.builder()
                .id(dbExecutor.getId())
                .deleted((byte) 1)
                .build();
        executorRepository.saveAndFlush(executor);

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
                Integer dbExecCode = jobLogRepository.findExecCodeById(logId);
                // 非 null，说明此任务已经回调过了
                if (dbExecCode != null) {
                    continue;
                }

                LocalDateTime beginExecTime = param.getBeginExecTime();
                LocalDateTime endExecTime = param.getEndExecTime();

                // 更新 JobLog 执行结果
                JobLog updateLog = JobLog.builder()
                        .id(logId)
                        .execCode(param.getExecCode())
                        .execMsg(param.getExecMsg())
                        .execBeginTime(param.getBeginExecTime())
                        .execEndTime(param.getEndExecTime())
                        .build();
                if (beginExecTime != null && endExecTime != null) {
                    Duration duration = Duration.between(beginExecTime, endExecTime);
                    updateLog.setExecCostTime((int) duration.toMillis());
                }
                jobLogRepository.saveAndFlush(updateLog);
            }
        });
        return ResultT.SUCCESS;
    }

}
