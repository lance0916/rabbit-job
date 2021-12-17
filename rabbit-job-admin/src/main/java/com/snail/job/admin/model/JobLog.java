package com.snail.job.admin.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("job_log")
public class JobLog extends Model<JobLog> {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 任务，主键ID
     */
    @TableField("job_id")
    private Long jobId;

    /**
     * 任务组名
     */
    @TableField("app_name")
    private String appName;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 执行-地址
     */
    @TableField("exec_address")
    private String execAddress;

    /**
     * 执行-handler
     */
    @TableField("exec_handler")
    private String execHandler;

    /**
     * 执行-参数
     */
    @TableField("exec_param")
    private String execParam;

    /**
     * 执行-失败重试次数
     */
    @TableField("fail_retry_count")
    private Integer failRetryCount;

    /**
     * 调度-时间
     */
    @TableField("trigger_time")
    private LocalDateTime triggerTime;

    /**
     * 调度-类型
     */
    @TableField("trigger_type")
    private String triggerType;

    /**
     * 调度-结果码
     */
    @TableField("trigger_code")
    private Integer triggerCode;

    /**
     * 调度-结果信息
     */
    @TableField("trigger_msg")
    private String triggerMsg;

    /**
     * 执行-结果码
     */
    @TableField("exec_code")
    private Integer execCode;

    /**
     * 执行-结果信息
     */
    @TableField("exec_msg")
    private String execMsg;

    /**
     * 执行-开始时间
     */
    @TableField("exec_begin_time")
    private LocalDateTime execBeginTime;

    /**
     * 执行-结束时间
     */
    @TableField("exec_end_time")
    private LocalDateTime execEndTime;

    /**
     * 执行-耗时
     */
    @TableField("exec_cost_time")
    private Integer execCostTime;

    /**
     * 告警状态：0-默认、1-无需告警、2-告警成功、3-告警失败
     */
    @TableField("alarm_status")
    private Integer alarmStatus;


    public static final String ID = "id";

    public static final String JOB_ID = "job_id";

    public static final String APP_NAME = "app_name";

    public static final String CREATE_TIME = "create_time";

    public static final String EXEC_ADDRESS = "exec_address";

    public static final String EXEC_HANDLER = "exec_handler";

    public static final String EXEC_PARAM = "exec_param";

    public static final String FAIL_RETRY_COUNT = "fail_retry_count";

    public static final String TRIGGER_TIME = "trigger_time";

    public static final String TRIGGER_TYPE = "trigger_type";

    public static final String TRIGGER_CODE = "trigger_code";

    public static final String TRIGGER_MSG = "trigger_msg";

    public static final String EXEC_CODE = "exec_code";

    public static final String EXEC_MSG = "exec_msg";

    public static final String EXEC_BEGIN_TIME = "exec_begin_time";

    public static final String EXEC_END_TIME = "exec_end_time";

    public static final String EXEC_COST_TIME = "exec_cost_time";

    public static final String ALARM_STATUS = "alarm_status";

    @Override
    public Serializable pkVal() {
        return this.id;
    }

}
