package com.snail.job.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.snail.job.admin.bean.req.JobLogQueryReq;
import com.snail.job.admin.bean.vo.JobLogVO;
import com.snail.job.admin.service.JobLogService;
import com.snail.job.common.model.ResultT;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author WuQinglong
 */
@RestController
@RequestMapping("/log")
public class LogController {

    @Resource
    private JobLogService jobLogService;

    /**
     * 分页查询
     */
    @GetMapping
    public ResultT<?> list(JobLogQueryReq request) {
        IPage<JobLogVO> page = jobLogService.listByPage(request);
        return new ResultT<>(page);
    }

}
