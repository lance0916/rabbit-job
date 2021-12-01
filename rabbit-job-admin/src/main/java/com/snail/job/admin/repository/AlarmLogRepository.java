package com.snail.job.admin.repository;

import com.snail.job.admin.entity.AlarmLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author WuQinglong created on 2021/11/27 20:13
 */
@Repository
public interface AlarmLogRepository extends JpaRepository<AlarmLog, Long> {
}