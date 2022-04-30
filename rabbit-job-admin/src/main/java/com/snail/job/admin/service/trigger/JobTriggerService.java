package com.snail.job.admin.service.trigger;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.snail.job.admin.biz.JobExecutorBiz;
import com.snail.job.admin.model.App;
import com.snail.job.admin.model.JobInfo;
import com.snail.job.admin.model.JobLog;
import com.snail.job.admin.route.AbstractRoute;
import com.snail.job.admin.route.RouteEnum;
import com.snail.job.admin.service.AppService;
import com.snail.job.admin.service.JobInfoService;
import com.snail.job.admin.service.JobLogService;
import com.snail.job.common.enums.AlarmStatus;
import com.snail.job.common.enums.TriggerType;
import com.snail.job.common.model.ResultT;
import com.snail.job.common.model.TriggerParam;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 进行任务的调度
 * @author WuQinglong
 */
@Slf4j
@Component
public class JobTriggerService {

    @Resource
    private JobExecutorBiz jobExecutorBiz;
    @Resource
    private JobInfoService jobInfoService;
    @Resource
    private AppService appService;
    @Resource
    private JobLogService jobLogService;

    /**
     * 触发 Job
     * @param jobId                  任务的id
     * @param triggerType            触发类型
     * @param overrideFailRetryCount 如果指定则使用该值，-1表示不指定
     * @param overrideExecParam      如果指定则使用改制，null表示不指定
     */
    public void trigger(Long jobId, Integer overrideFailRetryCount, String overrideExecParam, TriggerType triggerType) {
        // 查询任务信息
        JobInfo jobInfo = jobInfoService.getById(jobId);
        if (jobInfo == null) {
            log.error("任务不存在，jobId={}", jobId);
            return;
        }

        // 优先使用传入的调度参数
        if (overrideExecParam != null) {
            jobInfo.setExecParam(overrideExecParam);
        }

        // 优先使用参数的值
        if (overrideFailRetryCount != null && overrideFailRetryCount >= 0) {
            jobInfo.setExecFailRetryCount(overrideFailRetryCount);
        }

        // 获取执行器地址列表
        App app = appService.getOne(Wrappers.<App>query().eq(App.NAME, jobInfo.getAppName()));
        String addresses = app.getAddresses();
        String[] addressArray = addresses.split(StrUtil.COMMA);

        // 执行调度
        if (RouteEnum.BROADCAST.getName().equals(jobInfo.getExecRouteStrategy())) {
            // 广播执行
            int shardTotal = addressArray.length;
            for (int i = 0; i < addressArray.length; i++) {
                String executorAddress = addressArray[i];

                // 进行调度
                doTrigger(jobInfo, executorAddress, triggerType, i, shardTotal);
            }
        } else {
            // 非广播执行，选择一个执行器执行
            AbstractRoute route = RouteEnum.match(jobInfo.getExecRouteStrategy());
            String executorAddress = route.getExecutorAddress(app.getId(), jobId, addressArray);

            // 进行调度
            doTrigger(jobInfo, executorAddress, triggerType, 1, 1);
        }
    }

    /**
     * 进行调度
     */
    private void doTrigger(JobInfo jobInfo, String executorAddress, TriggerType triggerType,
                           Integer shareIndex, Integer shareTotal) {
        // 预先插入日志，获取日志id
        JobLog jobLog = new JobLog()
                .setJobId(jobInfo.getId())
                .setAppName(jobInfo.getAppName())
                .setTriggerType(triggerType.name());
        jobLogService.save(jobLog);

        // 初始化触发参数
        TriggerParam tp = new TriggerParam();
        tp.setLogId(jobLog.getId());
        tp.setJobId(jobInfo.getId());
        tp.setExecHandler(jobInfo.getExecHandler());
        tp.setExecParam(jobInfo.getExecParam());
        tp.setExecTimeout(jobInfo.getExecTimeout());
        tp.setShardIndex(shareIndex);
        tp.setShardIndex(shareTotal);

        log.info("调度任务 id={} handler={}", jobInfo.getId(), jobInfo.getExecHandler());

        // 触发远程执行器
        ResultT<String> triggerResult;
        if (StrUtil.isEmpty(executorAddress)) {
            triggerResult = new ResultT<>(ResultT.FAIL_CODE, "未找到可用的执行器！");
        } else {
            // 执行器那边的调度是异步的
            triggerResult = jobExecutorBiz.run(executorAddress, tp);
        }

        // 更新日志的调度信息
        JobLog updateLog = new JobLog()
                .setId(jobLog.getId())
                // 执行参数
                .setExecAddress(executorAddress)
                .setExecHandler(jobInfo.getExecHandler())
                .setExecParam(jobInfo.getExecParam())
                .setFailRetryCount(jobInfo.getExecFailRetryCount())
                // 调度信息
                .setTriggerTime(DateUtil.dateSecond())
                .setTriggerCode(triggerResult.getCode())
                .setTriggerMsg(triggerResult.getMsg())
                // 告警状态
                .setAlarmStatus(AlarmStatus.WAIT_ALARM.getValue());
        // execTime、execCode和execMsg 在回调中填充
        jobLogService.updateById(updateLog);
    }

}
