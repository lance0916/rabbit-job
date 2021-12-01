package com.snail.job.admin.repository;

import com.snail.job.admin.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author WuQinglong created on 2021/11/27 20:13
 */
@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long>, JpaSpecificationExecutor<Application> {

    /**
     * 根据名字查询应用
     * @param name 应用名字
     * @return 应用实例
     */
    Application findFirstByNameEquals(String name);

    /**
     * 列出所有的应用名
     * @return 应用集合
     */
    @Query("SELECT id, name, title FROM Application")
    List<Application> findAllNameAndTitle();

}