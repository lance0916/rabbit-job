package com.snail.job.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snail.job.admin.bean.vo.JobLogVO;
import com.snail.job.admin.mapper.JobLogMapper;
import com.snail.job.admin.model.JobLog;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 * @author WuQinglong
 * @since 2021-12-06
 */
@Service
public class JobLogService extends ServiceImpl<JobLogMapper, JobLog> {

    @Resource
    private JobLogMapper jobLogMapper;

    public List<JobLog> findAllFailJobLog() {
        return jobLogMapper.findAllFailJobLog();
    }

    public IPage<JobLogVO> listByPage(String appName, Integer jobId, Integer triggerCode, Integer execCode,
                                      Date triggerBeginDate, Date triggerEndDate, Integer pageNum, Integer pageSize) {
        return null;
    }

}
