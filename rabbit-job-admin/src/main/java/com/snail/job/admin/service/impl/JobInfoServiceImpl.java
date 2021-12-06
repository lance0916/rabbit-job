package com.snail.job.admin.service.impl;

import com.snail.job.admin.model.JobInfo;
import com.snail.job.admin.mapper.JobInfoMapper;
import com.snail.job.admin.service.IJobInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author WuQinglong
 * @since 2021-12-06
 */
@Service
public class JobInfoServiceImpl extends ServiceImpl<JobInfoMapper, JobInfo> implements IJobInfoService {

}
