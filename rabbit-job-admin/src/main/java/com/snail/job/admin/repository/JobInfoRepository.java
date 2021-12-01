package com.snail.job.admin.repository;

import com.snail.job.admin.entity.JobInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author WuQinglong created on 2021/11/27 20:13
 */
@Repository
public interface JobInfoRepository extends JpaRepository<JobInfo, Long>, JpaSpecificationExecutor<JobInfo> {

    /**
     * 根据调度状态获取
     * @param triggerStatus   调度状态
     * @param nextTriggerTime 任务的下次调度时间
     * @return 任务集合
     */
    @Query("SELECT id FROM JobInfo WHERE triggerStatus=?1 AND nextTriggerTime<?2 ORDER BY nextTriggerTime DESC")
    List<JobInfo> findAllWaitTriggerJob(Byte triggerStatus, Long nextTriggerTime);

    /**
     * 查询应用下的所有任务
     * @param appName 应用名
     * @return 任务集合
     */
    @Query("SELECT id, name FROM JobInfo WHERE appName=?1")
    List<JobInfo> findAllByAppName(String appName);

}