package com.snail.job.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.snail.job.admin.bean.req.JobLogQueryReq;
import com.snail.job.admin.bean.vo.JobLogVO;
import com.snail.job.admin.model.JobLog;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * Mapper 接口
 * </p>
 * @author WuQinglong
 * @since 2021-12-06
 */
public interface JobLogMapper extends BaseMapper<JobLog> {

    @Select({
            "<script>",
            "SELECT jl.*, ji.name AS jobName FROM job_log jl",
            "LEFT JOIN job_info ji ON jl.job_id=ji.id",
            "<where>",
            "   <if test=\"req.appName != null and req.appName != ''\">",
            "       AND jl.app_name LIKE CONCAT('%', #{req.appName}, '%'))",
            "   </if>",
            "   <if test=\"req.jobId != null\">",
            "       AND jl.job_id = #{req.jobId}",
            "   </if>",
            "   <if test=\"req.triggerCode != null\">",
            "       AND jl.trigger_code = #{req.triggerCode}",
            "   </if>",
            "   <if test=\"req.triggerBeginDate != null and req.triggerEndDate != null\">",
            "       AND jl.trigger_time BETWEEN #{req.triggerBeginDate} AND #{req.triggerEndDate}",
            "   </if>",
            "</where>",
            "</script>",
    })
    IPage<JobLogVO> listByPage(IPage<?> page, @Param("req") JobLogQueryReq request);

}
