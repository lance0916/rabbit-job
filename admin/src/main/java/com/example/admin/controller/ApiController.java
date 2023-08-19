package com.example.admin.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.admin.bean.entity.Executor;
import com.example.admin.bean.entity.App;
import com.example.admin.bean.entity.JobLog;
import com.example.admin.service.AppService;
import com.example.admin.service.ExecutorService;
import com.example.admin.service.JobLogService;
import com.example.common.aspect.annotation.CheckServiceAvailable;
import com.example.common.aspect.annotation.CheckSign;
import com.example.common.model.CallbackParam;
import com.example.common.model.RegistryParam;
import com.example.common.model.ResultT;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
@CheckServiceAvailable
public class ApiController {

    @Resource(name = "apiThreadPool")
    private ThreadPoolExecutor apiThreadPool;
    @Resource
    private ExecutorService executorService;
    @Resource
    private JobLogService jobLogService;
    @Resource
    private AppService appService;

    /**
     * 心跳
     */
    @PostMapping("/beat")
    public ResponseEntity<?> beat() {
        return ResponseEntity.ok(ResultT.SUCCESS);
    }

    /**
     * 注册执行器
     */
    @PostMapping("/registry")
    public ResponseEntity<?> registry(@RequestBody RegistryParam param) {
        String appName = param.getAppName();
        String address = param.getAddress();
        if (StrUtil.isBlank(appName) || StrUtil.isBlank(address)) {
            return ResponseEntity.ok(new ResultT<>(ResultT.FAIL_CODE, "参数缺失"));
        }

        // 关联应用是否存在
        long count = appService.count(Wrappers.<App>query()
                .eq(App.NAME, appName));
        if (count == 0) {
            return ResponseEntity.ok(new ResultT<>(ResultT.FAIL_CODE, "应用不存在"));
        }

        // 查询是否存在
        Executor executor = executorService.getOne(Wrappers.<Executor>query()
                .eq(Executor.APP_NAME, appName)
                .eq(Executor.ADDRESS, address)
        );
        if (executor == null) {
            executor = new Executor()
                    .setAppName(appName)
                    .setAddress(address);
        } else {
            executor.setDeleted(0)
                    .setUpdateTime(new Date());
        }
        executorService.saveOrUpdate(executor);
        return ResponseEntity.ok(ResultT.SUCCESS);
    }

    /**
     * 移除执行器
     */
    @PostMapping("/remove")
    public ResponseEntity<?> remove(@RequestBody RegistryParam param) {
        String appName = param.getAppName();
        String address = param.getAddress();
        if (StrUtil.isBlank(appName) || StrUtil.isBlank(address)) {
            return ResponseEntity.ok(new ResultT<>(ResultT.FAIL_CODE, "参数缺失"));
        }

        // 查询是否存在
        Executor executor = executorService.getOne(Wrappers.<Executor>query()
                .eq(Executor.APP_NAME, appName)
                .eq(Executor.ADDRESS, address)
        );
        if (executor == null) {
            return ResponseEntity.ok(new ResultT<>(ResultT.FAIL_CODE, "执行器不存在"));
        }

        // 从 executor 表中移除，并从执行地址中移除
        executor.setDeleted(1)
                .setUpdateTime(new Date());
        executorService.updateById(executor);
        return ResponseEntity.ok(ResultT.SUCCESS);
    }

    /**
     * 任务回调
     */
    @PostMapping("/callback")
    public ResponseEntity<?> callback(@RequestBody List<CallbackParam> list) {
        for (CallbackParam param : list) {
            Long logId = param.getLogId();
            Integer execCode = param.getExecCode();
            if (logId == null || execCode == null) {
                return ResponseEntity.ok(new ResultT<>(ResultT.FAIL_CODE, "参数缺失"));
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

                // 更新 JobLog 执行结果
                JobLog updateLog = new JobLog()
                        .setId(logId)
                        .setExecCode(param.getExecCode())
                        .setExecMsg(param.getExecMsg())
                        .setExecBeginTime(param.getBeginExecTime())
                        .setExecEndTime(param.getEndExecTime());
                Date beginExecTime = param.getBeginExecTime();
                Date endExecTime = param.getEndExecTime();
                if (beginExecTime != null && endExecTime != null) {
                    updateLog.setExecCostTime(endExecTime.getTime() - beginExecTime.getTime());
                }
                jobLogService.updateById(updateLog);
            }
        });
        return ResponseEntity.ok(ResultT.SUCCESS);
    }

}
