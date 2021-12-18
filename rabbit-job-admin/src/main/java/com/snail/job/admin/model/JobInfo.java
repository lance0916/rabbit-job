package com.snail.job.admin.model;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

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
public class JobInfo extends Model<JobInfo> {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 任务名称
     */
    @TableField("name")
    private String name;

    /**
     * 应用名称
     */
    @TableField("app_name")
    private String appName;

    /**
     * CRON表达式
     */
    @TableField("cron")
    private String cron;

    /**
     * 是否删除
     */
    @TableField("deleted")
    private Integer deleted;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;

    /**
     * 负责人姓名
     */
    @TableField("author_name")
    private String authorName;

    /**
     * 负责人邮箱
     */
    @TableField("author_email")
    private String authorEmail;

    /**
     * 执行路由策略
     */
    @TableField("exec_route_strategy")
    private String execRouteStrategy;

    /**
     * 执行任务handler
     */
    @TableField("exec_handler")
    private String execHandler;

    /**
     * 执行任务参数
     */
    @TableField("exec_param")
    private String execParam;

    /**
     * 执行超时时间，单位秒
     */
    @TableField("exec_timeout")
    private Integer execTimeout;

    /**
     * 失败重试次数
     */
    @TableField("exec_fail_retry_count")
    private Integer execFailRetryCount;

    /**
     * 调度状态。0-停止，1-运行
     */
    @TableField("trigger_status")
    private Integer triggerStatus;

    /**
     * 上次调度时间
     */
    @TableField("trigger_prev_time")
    private LocalDateTime triggerPrevTime;

    /**
     * 下次调度时间
     */
    @TableField("trigger_next_time")
    private LocalDateTime triggerNextTime;

    public static final String ID = "id";

    public static final String NAME = "name";

    public static final String APP_NAME = "app_name";

    public static final String CRON = "cron";

    public static final String DELETED = "deleted";

    public static final String CREATE_TIME = "create_time";

    public static final String UPDATE_TIME = "update_time";

    public static final String AUTHOR_NAME = "author_name";

    public static final String AUTHOR_EMAIL = "author_email";

    public static final String EXEC_ROUTE_STRATEGY = "exec_route_strategy";

    public static final String EXEC_HANDLER = "exec_handler";

    public static final String EXEC_PARAM = "exec_param";

    public static final String EXEC_TIMEOUT = "exec_timeout";

    public static final String EXEC_FAIL_RETRY_COUNT = "exec_fail_retry_count";

    public static final String TRIGGER_STATUS = "trigger_status";

    public static final String TRIGGER_PREV_TIME = "trigger_prev_time";

    public static final String TRIGGER_NEXT_TIME = "trigger_next_time";

    @Override
    public Serializable pkVal() {
        return this.id;
    }

}
