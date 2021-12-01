package com.snail.job.admin.repository;

import com.snail.job.admin.entity.JobLogReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

/**
 * @author WuQinglong created on 2021/11/27 20:13
 */
@Repository
public interface JobLogReportRepository extends JpaRepository<JobLogReport, Integer> {

    /**
     * 查询今日的统计任务
     * @param today 今日
     * @return 统计集合
     */
    JobLogReport findAllByTriggerDateEquals(LocalDate today);

}