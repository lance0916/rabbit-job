package com.snail.job.admin.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 * @author WuQinglong
 * @since 2021-12-06
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("job_info")
public class JobInfo {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 任务名称
     */
    private String name;

    /**
     * 应用名称
     */
    private String appName;

    /**
     * CRON表达式
     */
    private String cron;

    /**
     * 是否删除
     */
    private Integer deleted;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 负责人姓名
     */
    private String authorName;

    /**
     * 负责人邮箱
     */
    private String authorEmail;

    /**
     * 执行路由策略
     */
    private String execRouteStrategy;

    /**
     * 执行任务handler
     */
    private String execHandler;

    /**
     * 执行任务参数
     */
    private String execParam;

    /**
     * 执行超时时间，单位秒
     */
    private Integer execTimeout;

    /**
     * 失败重试次数
     */
    private Integer execFailRetryCount;

    /**
     * 调度状态。0-停止，1-运行
     */
    private Integer triggerStatus;

    /**
     * 上次调度时间
     */
    private LocalDateTime triggerPrevTime;

    /**
     * 下次调度时间
     */
    private LocalDateTime triggerNextTime;

}
