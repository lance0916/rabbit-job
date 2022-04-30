package com.snail.job.admin.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.snail.job.admin.bean.req.JobInfoQueryReq;
import com.snail.job.admin.bean.vo.RouteVO;
import com.snail.job.admin.model.JobInfo;
import com.snail.job.admin.route.RouteEnum;
import com.snail.job.admin.service.JobInfoService;
import com.snail.job.admin.service.trigger.CronExpression;
import com.snail.job.admin.service.trigger.TriggerPoolService;
import com.snail.job.common.enums.TriggerType;
import com.snail.job.common.model.ResultT;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author WuQinglong
 */
@RestController
@RequestMapping("/job")
public class JobInfoController {

    @Resource
    private JobInfoService jobInfoService;
    @Resource
    private TriggerPoolService triggerPoolService;

    /**
     * 分页查询
     */
    @GetMapping
    public ResultT<?> list(JobInfoQueryReq request) {
        IPage<JobInfo> page = jobInfoService.listByPage(request);
        return new ResultT<>(page);
    }

    /**
     * 新增
     */
    @PostMapping
    public ResultT<?> save(@RequestBody JobInfo jobInfo) {
        // Cron 表达式是否正确
        Assert.isTrue(CronExpression.isValidExpression(jobInfo.getCron()), "Cron表达式不正确");
        jobInfoService.save(jobInfo);
        return ResultT.SUCCESS;
    }

    /**
     * 更新
     */
    @PutMapping
    public ResultT<String> update(@RequestBody JobInfo jobInfo) {
        // Cron 表达式是否正确
        Assert.isTrue(CronExpression.isValidExpression(jobInfo.getCron()), "Cron表达式不正确");
        jobInfoService.updateById(jobInfo);
        return ResultT.SUCCESS;
    }

    /**
     * 删除
     */
    @DeleteMapping("/{id}")
    public ResultT<?> delete(@PathVariable("id") Long id) {
        jobInfoService.delete(id);
        return ResultT.SUCCESS;
    }

    /**
     * 启动任务
     */
    @PostMapping("/start/{id}")
    public ResultT<?> start(@PathVariable("id") Long id) {
        jobInfoService.start(id);
        return ResultT.SUCCESS;
    }

    /**
     * 停止任务
     */
    @PostMapping("/stop/{id}")
    public ResultT<?> stop(@PathVariable("id") Long id) {
        jobInfoService.stop(id);
        return ResultT.SUCCESS;
    }

    /**
     * 执行一次
     */
    @PostMapping("/exec/{id}")
    public ResultT<?> exec(@PathVariable("id") Long id, String execParam) {
        triggerPoolService.trigger(id, -1, execParam, TriggerType.API);
        return ResultT.SUCCESS;
    }

    /**
     * 获取路由策略
     */
    @GetMapping("/route")
    public ResultT<?> route() {
        List<RouteVO> list = new ArrayList<>();
        RouteEnum[] routeEnums = RouteEnum.values();
        for (RouteEnum strategyEnum : routeEnums) {
            list.add(new RouteVO(strategyEnum.getName(), strategyEnum.getDesc()));
        }
        return new ResultT<>(list);
    }

    /**
     * 下次执行时间
     */
    @GetMapping("/nextTriggerTime")
    public ResultT<?> triggerNextTime(String cron) {
        CronExpression cronExpression;
        try {
            cronExpression = new CronExpression(cron);
        } catch (Exception e) {
            return new ResultT<>(ResultT.FAIL_CODE, "Cron表达式有误");
        }

        Date curDate = new Date();
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Date next = cronExpression.getTimeAfter(curDate);
            if (next != null) {
                list.add(DateUtil.formatDateTime(next));
            }
            curDate = next;
        }
        return new ResultT<>(list);
    }

    @GetMapping("/listByAppName")
    public ResultT<?> listByAppName(String appName) {
        List<JobInfo> list = jobInfoService.listByAppName(appName);
        return new ResultT<>(list);
    }

}
