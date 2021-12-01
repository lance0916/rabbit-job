package com.snail.job.admin.service;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.snail.job.admin.entity.JobInfo;
import com.snail.job.admin.repository.JobInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static com.snail.job.admin.constant.AdminConstants.SCAN_JOB_SLEEP_MS;
import static com.snail.job.common.enums.TriggerStatus.RUNNING;
import static com.snail.job.common.enums.TriggerStatus.STOPPED;

/**
 * @author 吴庆龙
 * @date 2020/7/27 10:37 上午
 */
@Slf4j
@Service
public class JobInfoService {

    @Resource
    private JobInfoRepository jobInfoRepository;

    /**
     * 分页查询
     */
    public Page<JobInfo> list(String name, String appName, String authorName, Byte triggerStatus,
                              Integer pageNum, Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNum, pageSize);
        return jobInfoRepository.findAll((Specification<JobInfo>) (root, query, builder) -> {
            Predicate predicate = builder.conjunction();
            List<Expression<Boolean>> expressions = predicate.getExpressions();
            if (StrUtil.isNotEmpty(name)) {
                expressions.add(builder.like(root.get("name"), "%" + name + "%"));
            }
            if (StrUtil.isNotEmpty(appName)) {
                expressions.add(builder.like(root.get("appName"), "%" + appName + "%"));
            }
            if (StrUtil.isNotEmpty(authorName)) {
                expressions.add(builder.like(root.get("authorName"), "%" + authorName + "%"));
            }
            if (triggerStatus != null) {
                expressions.add(builder.equal(root.get("triggerStatus"), triggerStatus));
            }
            return predicate;
        }, pageRequest);
    }

    /**
     * 开始运行任务
     */
    public void start(Long id) {
        Optional<JobInfo> jobInfoOptional = jobInfoRepository.findById(id);
        if (!jobInfoOptional.isPresent()) {
            log.warn("任务不存在。id={}", id);
            return;
        }
        JobInfo jobInfo = jobInfoOptional.get();

        // 计算任务的下次执行时间
        CronExpression cronExpression = CronExpression.parse(jobInfo.getCron());

        // 计算下次执行时间
        Instant instant = Instant.now(Clock.systemDefaultZone());
        instant.plus(SCAN_JOB_SLEEP_MS, ChronoUnit.MILLIS);
        Instant nextTriggerTime = cronExpression.next(instant);

        // 更新
        JobInfo updateJobInfo = new JobInfo();
        updateJobInfo.setId(jobInfo.getId());
        if (nextTriggerTime != null) {
            updateJobInfo.setNextTriggerTime(nextTriggerTime.getEpochSecond());
            updateJobInfo.setTriggerStatus(RUNNING.getValue());
        } else {
            updateJobInfo.setPrevTriggerTime(0L);
            updateJobInfo.setNextTriggerTime(0L);
            updateJobInfo.setTriggerStatus(STOPPED.getValue());
        }
        jobInfoRepository.saveAndFlush(updateJobInfo);
    }

    /**
     * 停止任务
     */
    public void stop(Long id) {
        Optional<JobInfo> jobInfoOptional = jobInfoRepository.findById(id);
        if (!jobInfoOptional.isPresent()) {
            log.warn("任务不存在。id={}", id);
            return;
        }
        JobInfo jobInfo = jobInfoOptional.get();

        // 更新
        JobInfo updateJobInfo = new JobInfo();
        updateJobInfo.setId(jobInfo.getId());
        updateJobInfo.setNextTriggerTime(0L);
        updateJobInfo.setTriggerStatus(STOPPED.getValue());
        jobInfoRepository.saveAndFlush(updateJobInfo);
    }

    /**
     * 保存 或 更新
     */
    public void saveOrUpdate(JobInfo info) {
        // Cron 表达式是否正确
        Assert.isTrue(CronExpression.isValidExpression(info.getCron()), "Cron表达式不正确");

        if (info.getId() == null) {
            info.setCreateTime(LocalDateTime.now());
        } else {
            info.setUpdateTime(LocalDateTime.now());
        }
        jobInfoRepository.saveAndFlush(info);
    }

    /**
     * 删除
     * TODO 执行软删
     */
    public void delete(Long id) {
        jobInfoRepository.deleteById(id);
    }

    /**
     * 列出分组下的所有任务
     */
    public List<JobInfo> listByAppName(String appName) {
        return jobInfoRepository.findAllByAppName(appName);
    }
}
