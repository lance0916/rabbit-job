package com.snail.job.admin.controller;

import com.snail.job.admin.controller.vo.JobLogVO;
import com.snail.job.admin.service.LogService;
import com.snail.job.common.model.ResultT;
import org.springframework.data.domain.Page;
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
    private LogService logService;

    /**
     * 分页查询
     */
    @GetMapping
    public ResultT<?> list(String appName, Integer jobId, Integer triggerCode, Integer execCode,
                           Date triggerBeginDate, Date triggerEndDate,
                           @RequestParam Integer pageNum, @RequestParam Integer pageSize) {
        Page<JobLogVO> page = logService.list(appName, jobId, triggerCode, execCode,
                triggerBeginDate, triggerEndDate, pageNum, pageSize);
        return new ResultT<>(page);
    }

}
