package com.snail.job.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.snail.job.admin.model.App;
import com.snail.job.admin.service.AppService;
import com.snail.job.common.model.ResultT;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 吴庆龙
 */
@RestController
@RequestMapping("/app")
public class AppController {

    @Resource
    private AppService appService;

    /**
     * 分页列表
     */
    @GetMapping
    public ResultT<?> list(String name, String title,
                           @RequestParam(name = "page") Integer pageNum,
                           @RequestParam(name = "limit") Integer pageSize) {
        IPage<App> page = appService.page(name, title, pageNum, pageSize);
        return new ResultT<>(page);
    }

    /**
     * 新增
     */
    @PostMapping
    public ResultT<?> save(@RequestBody App jobApp) {
        App app = appService.selectByName(jobApp.getName());

        // 校验 name 是否有相同的
        Assert.isTrue(app == null, "name已经存在");

        appService.save(jobApp);
        return ResultT.SUCCESS;
    }

    /**
     * 更新
     */
    @PutMapping
    public ResultT<?> update(@RequestBody App app) {
        App appDB = appService.getById(app.getId());

        // 校验 name 是否有相同的
        Assert.isTrue(appDB != null && !appDB.getId().equals(app.getId()), "name已经存在");

        appService.updateById(app);
        return ResultT.SUCCESS;
    }

    /**
     * 删除
     */
    @DeleteMapping("/{id}")
    public ResultT<?> delete(@PathVariable("id") Long id) {
        appService.delete(id);
        return ResultT.SUCCESS;
    }

    /**
     * 列出所有的应用名
     */
    @GetMapping("/listAll")
    public ResultT<?> listNameTitle() {
        List<App> list = appService.findAllNameAndTitle();
        return new ResultT<>(list);
    }

}
