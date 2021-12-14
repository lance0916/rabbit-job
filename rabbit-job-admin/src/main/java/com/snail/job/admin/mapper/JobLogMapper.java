package com.snail.job.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.snail.job.admin.bean.request.JobLogQueryRequest;
import com.snail.job.admin.bean.vo.JobLogVO;
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

    @Select("")
    IPage<JobLogVO> listByPage(IPage<?> page, JobLogQueryRequest request);

}
