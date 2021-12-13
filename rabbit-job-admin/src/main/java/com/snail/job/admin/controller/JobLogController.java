package com.snail.job.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.snail.job.admin.bean.vo.JobLogVO;
import com.snail.job.admin.service.JobLogService;
import com.snail.job.common.model.ResultT;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author 吴庆龙
 * @date 2020/7/23 3:12 下午
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
    public ResultT<?> list(String appName, Integer jobId, Integer triggerCode, Integer execCode,
                           Date triggerBeginDate, Date triggerEndDate,
                           @RequestParam Integer pageNum, @RequestParam Integer pageSize) {
        IPage<JobLogVO> page = jobLogService.listByPage(appName, jobId, triggerCode, execCode,
                triggerBeginDate, triggerEndDate, pageNum, pageSize);
        return new ResultT<>(page);
    }

}
