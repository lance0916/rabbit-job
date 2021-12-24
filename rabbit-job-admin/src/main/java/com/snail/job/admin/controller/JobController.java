package com.snail.job.admin.controller;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.snail.job.admin.bean.request.JobInfoQueryRequest;
import com.snail.job.admin.bean.vo.RouteVO;
import com.snail.job.admin.model.JobInfo;
import com.snail.job.admin.route.RouteEnum;
import com.snail.job.admin.service.JobInfoService;
import com.snail.job.admin.service.trigger.TriggerPoolService;
import com.snail.job.common.enums.TriggerType;
import com.snail.job.common.model.ResultT;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 吴庆龙
 * @date 2020/7/23 3:12 下午
 */
@RestController
@RequestMapping("/job")
public class JobController {

    @Resource
    private JobInfoService jobInfoService;
    @Resource
    private TriggerPoolService triggerPoolService;

    /**
     * 分页查询
     */
    @GetMapping
    public ResultT<?> list(JobInfoQueryRequest request) {
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

        jobInfo.setCreateTime(LocalDateTime.now());
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

        jobInfo.setUpdateTime(LocalDateTime.now());
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
        List<String> list = new ArrayList<>();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        CronExpression cronExpression = CronExpression.parse(cron);
        LocalDateTime dateTime = LocalDateTime.now();
        for (int i = 0; i < 5; i++) {
            LocalDateTime next = cronExpression.next(dateTime);
            if (next != null) {
                list.add(dateTimeFormatter.format(next));
            }
            dateTime = next;
        }
        return new ResultT<>(list);
    }

    @GetMapping("/listByAppName")
    public ResultT<?> listByAppName(String appName) {
        List<JobInfo> list = jobInfoService.listByAppName(appName);
        return new ResultT<>(list);
    }

}
