package com.snail.job.admin.repository;

import com.snail.job.admin.entity.Executor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author WuQinglong created on 2021/11/27 20:13
 */
@Repository
public interface ExecutorRepository extends JpaRepository<Executor, Long> {

    /**
     * 根据 appName 和 address 查询唯一一条记录
     * @param appName 应用名
     * @param address 地址
     * @return 记录的id，null 表示记录不存在
     */
    Executor findFirstByAppNameEqualsAndAddressEquals(String appName, String address);

}