package com.example.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.admin.bean.req.JobLogQueryReq;
import com.example.admin.bean.vo.JobLogVO;
import com.example.admin.service.JobLogService;
import com.example.common.model.ResultT;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author WuQinglong
 */
@RestController
@RequestMapping("/log")
public class JobLogController {

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
