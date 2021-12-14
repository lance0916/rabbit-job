package com.snail.job.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snail.job.admin.bean.request.JobLogQueryRequest;
import com.snail.job.admin.bean.vo.JobLogVO;
import com.snail.job.admin.mapper.JobLogMapper;
import com.snail.job.admin.model.JobLog;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 * @author WuQinglong
 * @since 2021-12-06
 */
@Service
public class JobLogService extends ServiceImpl<JobLogMapper, JobLog> {

    /**
     * 分页查询
     */
    public IPage<JobLogVO> listByPage(JobLogQueryRequest request) {
        IPage<JobLogVO> pageParam = new Page<>(request.getPageNum(), request.getPageSize());
        return getBaseMapper().listByPage(pageParam, request);
    }

}
