package com.snail.job.admin.controller;

import com.snail.job.admin.entity.Application;
import com.snail.job.admin.service.ApplicationService;
import com.snail.job.common.model.ResultT;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 吴庆龙
 * @date 2020/7/23 10:34 上午
 */
@RestController
@RequestMapping("/app")
public class ApplicationController {

    @Resource
    private ApplicationService applicationService;

    /**
     * 分页列表
     */
    @GetMapping
    public ResultT<?> list(String name, @RequestParam Integer pageNum, @RequestParam Integer pageSize) {
        Page<Application> page = applicationService.page(name, pageNum, pageSize);
        return new ResultT<>(page);
    }

    /**
     * 新增
     */
    @PostMapping
    public ResultT<?> save(@RequestBody Application jobApp) {
        applicationService.saveOrUpdate(jobApp);
        return ResultT.SUCCESS;
    }

    /**
     * 更新
     */
    @PutMapping
    public ResultT<?> update(@RequestBody Application jobApp) {
        applicationService.saveOrUpdate(jobApp);
        return ResultT.SUCCESS;
    }

    /**
     * 删除
     */
    @DeleteMapping("/{id}")
    public ResultT<?> delete(@PathVariable("id") Long id) {
        applicationService.delete(id);
        return ResultT.SUCCESS;
    }

    /**
     * 列出所有的应用名
     */
    @GetMapping("/listNameTitle")
    public ResultT<?> listNameTitle() {
        List<Application> list = applicationService.findAllNameAndTitle();
        return new ResultT<>(list);
    }

}
