package com.snail.job.admin.repository;

import com.snail.job.admin.entity.JobLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author WuQinglong created on 2021/11/27 20:13
 */
@Repository
public interface JobLogRepository extends JpaRepository<JobLog, Long>, JpaSpecificationExecutor<JobLog> {

    /**
     * 查询执行码
     * @param id 主键
     * @return 执行码，可能为 null
     */
    @Query("SELECT execCode FROM JobLog WHERE id=?1")
    Integer findExecCodeById(Long id);

    /**
     * 获取所有调度失败的任务日志id
     * @return 任务执行日志集合
     */
    @Query("SELECT id, jobId, failRetryCount FROM JobLog " +
            "WHERE alarmStatus=1 AND triggerCode NOT IN (0, 200) AND execCode NOT IN (0, 200)")
    List<JobLog> findAllFailJobLog();

    /**
     * 查询今日执行的任务
     * @param beginTime 开始时间
     * @param endTime 结束时间
     * @return 任务集合
     */
    @Query("SELECT id FROM JobLog WHERE triggerTime BETWEEN ?1 AND ?2")
    List<JobLog> findAllTodayJobLog(LocalDateTime beginTime, LocalDateTime endTime);

}