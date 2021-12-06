package com.snail.job.admin.service;

import com.snail.job.admin.model.JobLog;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author WuQinglong
 * @since 2021-12-06
 */
public interface IJobLogService extends IService<JobLog> {

    List<JobLog> findAllFailJobLog();
}
