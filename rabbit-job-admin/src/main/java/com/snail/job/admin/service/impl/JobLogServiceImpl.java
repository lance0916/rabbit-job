package com.snail.job.admin.service.impl;

import com.snail.job.admin.model.JobLog;
import com.snail.job.admin.mapper.JobLogMapper;
import com.snail.job.admin.service.IJobLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author WuQinglong
 * @since 2021-12-06
 */
@Service
public class JobLogServiceImpl extends ServiceImpl<JobLogMapper, JobLog> implements IJobLogService {

    @Resource
    private JobLogMapper jobLogMapper;

    @Override
    public List<JobLog> findAllFailJobLog() {
        return jobLogMapper.findAllFailJobLog();
    }

}
