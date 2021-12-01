package com.snail.job.admin.service;

import cn.hutool.core.util.StrUtil;
import com.snail.job.admin.entity.Application;
import com.snail.job.admin.repository.ApplicationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author 吴庆龙
 * @date 2020/7/23 3:14 下午
 */
@Service
public class ApplicationService {

    @Resource
    private ApplicationRepository applicationRepository;

    /**
     * 分页列表
     */
    public Page<Application> page(String name, Integer pageNum, Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNum, pageSize);
        return applicationRepository.findAll((Specification<Application>) (root, query, builder) -> {
            Predicate predicate = builder.conjunction();
            List<Expression<Boolean>> expressions = predicate.getExpressions();
            if (StrUtil.isNotEmpty(name)) {
                expressions.add(builder.like(root.get("name"), "%" + name + "%"));
            }
            return predicate;
        }, pageRequest);
    }

    /**
     * 保存或更新
     */
    public void saveOrUpdate(Application app) {
        Application application = applicationRepository.findFirstByNameEquals(app.getName());
        if (app.getId() == null) {
            // 校验 name 是否有相同的
            Assert.isTrue(application == null, "name已经存在");

            // 新增
            app.setCreateTime(LocalDateTime.now());
            applicationRepository.saveAndFlush(app);
        } else {
            // 校验 name 是否有相同的
            Assert.isTrue(application == null || !application.getId().equals(app.getId()), "name已经存在");

            // 更新
            app.setUpdateTime(LocalDateTime.now());
            applicationRepository.saveAndFlush(app);
        }
    }

    /**
     * 删除
     * TODO 修改为软删
     */
    public void delete(Long id) {
        applicationRepository.deleteById(id);
    }

    /**
     * 查询所有分组，供下拉框使用
     */
    public List<Application> findAllNameAndTitle() {
        return applicationRepository.findAllNameAndTitle();
    }
}
