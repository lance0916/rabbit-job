package com.snail.job.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.snail.job.admin.model.JobLog;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 * @author WuQinglong
 * @since 2021-12-06
 */
public interface JobLogMapper extends BaseMapper<JobLog> {

    @Select("SELECT id, job_id, fail_retry_count FROM job_log " +
            "WHERE alarm_status=1 AND trigger_code NOT IN (0, 200) AND exec_code NOT IN (0, 200)")
    List<JobLog> findAllFailJobLog();

}
