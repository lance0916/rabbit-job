package com.snail.job.admin.service.trigger;

import com.snail.job.admin.entity.Application;
import com.snail.job.admin.entity.JobInfo;
import com.snail.job.admin.entity.JobLog;
import com.snail.job.admin.biz.JobExecutorBiz;
import com.snail.job.admin.repository.ApplicationRepository;
import com.snail.job.admin.repository.JobInfoRepository;
import com.snail.job.admin.repository.JobLogRepository;
import com.snail.job.admin.route.ExecRouteStrategyEnum;
import com.snail.job.admin.route.ExecRouter;
import com.snail.job.common.enums.AlarmStatus;
import com.snail.job.common.enums.TriggerType;
import com.snail.job.common.model.ResultT;
import com.snail.job.common.model.TriggerParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 进行任务的调度
 * @author 吴庆龙
 * @date 2020/6/17 1:56 下午
 */
@Slf4j
@Component
public class JobTriggerService {

    @Resource
    private JobInfoRepository jobInfoRepository;
    @Resource
    private JobLogRepository jobLogRepository;
    @Resource
    private ApplicationRepository applicationRepository;
    @Resource
    private JobExecutorBiz jobExecutorBiz;
    @Resource
    private RouteStrategyService routeStrategyService;

    /**
     * 触发 Job
     * @param jobId                  任务的id
     * @param triggerType            触发类型
     * @param overrideFailRetryCount 如果指定则使用该值，-1表示不指定
     * @param overrideExecParam      如果指定则使用改制，null表示不指定
     */
    public void trigger(Long jobId, Integer overrideFailRetryCount, String overrideExecParam, TriggerType triggerType) {
        // 查询任务信息
        Optional<JobInfo> jobInfoOptional = jobInfoRepository.findById(jobId);
        if (!jobInfoOptional.isPresent()) {
            log.error("任务不存在，jobId={}", jobId);
            return;
        }
        JobInfo jobInfo = jobInfoOptional.get();

        // 优先使用传入的调度参数
        if (overrideExecParam != null) {
            jobInfo.setExecParam(overrideExecParam);
        }

        // 优先使用参数的值
        if (overrideFailRetryCount != null && overrideFailRetryCount >= 0) {
            jobInfo.setExecFailRetryCount(overrideFailRetryCount.byteValue());
        }

        // 获取执行器地址列表
        Application application = applicationRepository.findFirstByNameEquals(jobInfo.getAppName());
        String addresses = application.getAddresses();
        String[] addressArray = addresses.split(",");

        // 执行调度
        if (ExecRouteStrategyEnum.BROADCAST.getName().equals(jobInfo.getExecRouteStrategy())) {
            // 广播执行
            int shardTotal = addressArray.length;
            for (int i = 0; i < addressArray.length; i++) {
                String executorAddress = addressArray[i];

                // 进行调度
                doTrigger(jobInfo, executorAddress, triggerType, i, shardTotal);
            }
        } else {
            // 非广播执行，选择一个执行器执行
            ExecRouter router = routeStrategyService.match(jobInfo.getExecRouteStrategy());
            String executorAddress = router.route(jobId, addressArray);

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
        JobLog jobLog = JobLog.builder()
                .jobId(jobInfo.getId())
                .appName(jobInfo.getAppName())
                .triggerType(triggerType.name())
                .build();
        jobLogRepository.saveAndFlush(jobLog);

        // 初始化触发参数
        TriggerParam tp = new TriggerParam();
        tp.setLogId(jobLog.getId());
        tp.setJobId(jobInfo.getId());
        tp.setExecHandler(jobInfo.getExecHandler());
        tp.setExecParam(jobInfo.getExecParam());
        tp.setExecTimeout(jobInfo.getExecTimeout());
        tp.setShardIndex(shareIndex);
        tp.setShardIndex(shareTotal);

        // 触发远程执行器
        ResultT<String> triggerResult;
        if (executorAddress == null) {
            triggerResult = new ResultT<>(ResultT.FAIL_CODE, "未找到可用的执行器！");
        } else {
            triggerResult = jobExecutorBiz.run(executorAddress, tp);
        }

        // 更新日志的调度信息
        JobLog updateLog = JobLog.builder()
                .id(jobLog.getId())
                // 执行参数
                .execAddress(executorAddress)
                .execHandler(jobInfo.getExecHandler())
                .execParam(jobInfo.getExecParam())
                .failRetryCount(jobInfo.getExecFailRetryCount())
                // 调度信息
                .triggerCode(triggerResult.getCode())
                .triggerMsg(triggerResult.getMsg())
                .triggerTime(LocalDateTime.now())
                // 告警状态
                .alarmStatus(AlarmStatus.WAIT_ALARM.getValue())
                .build();
        // execTime、execCode和execMsg 在回调中填充
        jobLogRepository.saveAndFlush(updateLog);
    }

}
