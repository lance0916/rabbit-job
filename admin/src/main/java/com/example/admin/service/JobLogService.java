package com.example.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.admin.bean.entity.JobLog;
import com.example.admin.bean.req.JobLogQueryReq;
import com.example.admin.mapper.JobLogMapper;
import com.example.admin.bean.vo.JobLogVO;
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
    public IPage<JobLogVO> listByPage(JobLogQueryReq request) {
        IPage<JobLogVO> pageParam = new Page<>(request.getPageNum(), request.getPageSize());
        return getBaseMapper().listByPage(pageParam, request);
    }

}
