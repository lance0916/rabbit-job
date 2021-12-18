package com.snail.job.admin.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snail.job.admin.bean.request.JobInfoQueryRequest;
import com.snail.job.admin.mapper.JobInfoMapper;
import com.snail.job.admin.model.JobInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.snail.job.common.enums.TriggerStatus.RUNNING;
import static com.snail.job.common.enums.TriggerStatus.STOPPED;

/**
 * <p>
 * 服务实现类
 * </p>
 * @author WuQinglong
 * @since 2021-12-06
 */
@Slf4j
@Service
public class JobInfoService extends ServiceImpl<JobInfoMapper, JobInfo> {

    /**
     * 分页查询
     */
    public IPage<JobInfo> listByPage(JobInfoQueryRequest request) {
        QueryWrapper<JobInfo> queryWrapper = new QueryWrapper<>();
        if (StrUtil.isNotEmpty(request.getName())) {
            queryWrapper.like(JobInfo.NAME, request.getName());
        }
        if (StrUtil.isNotEmpty(request.getAppName())) {
            queryWrapper.like(JobInfo.APP_NAME, request.getAppName());
        }
        if (StrUtil.isNotEmpty(request.getAuthorName())) {
            queryWrapper.like(JobInfo.AUTHOR_NAME, request.getAuthorName());
        }
        if (request.getTriggerStatus() != null) {
            queryWrapper.eq(JobInfo.TRIGGER_STATUS, request.getTriggerStatus());
        }
        queryWrapper.eq(JobInfo.DELETED, 0);
        IPage<JobInfo> page = new Page<>(request.getPageNum(), request.getPageSize());
        return super.page(page, queryWrapper);
    }

    /**
     * 开始运行任务
     */
    public void start(Long id) {
        JobInfo jobInfo = super.getById(id);
        if (jobInfo == null) {
            log.warn("任务不存在。id={}", id);
            return;
        }

        // 计算任务的下次执行时间
        CronExpression cronExpression = CronExpression.parse(jobInfo.getCron());

        // 计算下次执行时间
        LocalDateTime nextTriggerTime = cronExpression.next(LocalDateTime.now());

        // 更新
        JobInfo updateJobInfo = new JobInfo();
        updateJobInfo.setId(jobInfo.getId());
        if (nextTriggerTime != null) {
            updateJobInfo.setTriggerNextTime(nextTriggerTime);
            updateJobInfo.setTriggerStatus(RUNNING.getValue());
        } else {
            updateJobInfo.setTriggerPrevTime(null);
            updateJobInfo.setTriggerNextTime(null);
            updateJobInfo.setTriggerStatus(STOPPED.getValue());
        }
        super.updateById(updateJobInfo);
    }

    /**
     * 停止任务
     */
    public void stop(Long id) {
        JobInfo jobInfo = super.getById(id);
        if (jobInfo == null) {
            log.warn("任务不存在。id={}", id);
            return;
        }

        // 更新
        JobInfo updateJobInfo = new JobInfo();
        updateJobInfo.setId(jobInfo.getId());
        updateJobInfo.setTriggerNextTime(null);
        updateJobInfo.setTriggerStatus(STOPPED.getValue());
        super.updateById(updateJobInfo);
    }

    /**
     * 删除
     */
    public void delete(Long id) {
        JobInfo jobInfo = new JobInfo()
                .setId(id)
                .setDeleted(1)
                .setUpdateTime(LocalDateTime.now());
        super.updateById(jobInfo);
    }

    /**
     * 列出分组下的所有任务
     */
    public List<JobInfo> listByAppName(String appName) {
        QueryWrapper<JobInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.select(JobInfo.ID, JobInfo.NAME)
                .eq(JobInfo.APP_NAME, appName)
                .eq(JobInfo.DELETED, 0);
        return super.list(queryWrapper);
    }

}
