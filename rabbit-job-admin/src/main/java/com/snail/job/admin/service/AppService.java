package com.snail.job.admin.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snail.job.admin.mapper.AppMapper;
import com.snail.job.admin.model.App;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 * @author WuQinglong
 * @since 2021-12-06
 */
@Service
public class AppService extends ServiceImpl<AppMapper, App> {

    /**
     * 分页列表
     */
    public IPage<App> page(String name, String title, Integer pageNum, Integer pageSize) {
        IPage<App> page = new Page<>(pageNum, pageSize);

        QueryWrapper<App> queryWrapper = new QueryWrapper<>();
        if (StrUtil.isNotEmpty(name)) {
            queryWrapper.like(App.NAME, name);
        }
        if (StrUtil.isNotEmpty(title)) {
            queryWrapper.like(App.TITLE, title);
        }
        queryWrapper.eq(App.DELETED, 0);
        return super.page(page, queryWrapper);
    }

    /**
     * 根据名字判断是否存在
     */
    public App selectByName(String name) {
        if (StrUtil.isEmpty(name)) {
            return null;
        }

        QueryWrapper<App> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(App.NAME, name);
        return super.getOne(queryWrapper);
    }

    /**
     * 删除
     */
    public void delete(Long id) {
        App app = new App()
                .setId(id)
                .setDeleted(1)
                .setUpdateTime(LocalDateTime.now());
        super.updateById(app);
    }

    /**
     * 查询所有分组，供下拉框使用
     */
    public List<App> findAllNameAndTitle() {
        QueryWrapper<App> queryWrapper = new QueryWrapper<>();
        queryWrapper.select(App.NAME, App.TITLE);
        return super.list(queryWrapper);
    }

}
