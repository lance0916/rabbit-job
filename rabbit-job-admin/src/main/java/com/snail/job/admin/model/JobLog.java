package com.snail.job.admin.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author WuQinglong
 * @since 2021-12-06
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("job_log")
public class JobLog {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 任务，主键ID
     */
    private Integer jobId;

    /**
     * 任务组名
     */
    private String appName;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 执行-地址
     */
    private String execAddress;

    /**
     * 执行-handler
     */
    private String execHandler;

    /**
     * 执行-参数
     */
    private String execParam;

    /**
     * 执行-失败重试次数
     */
    private Integer failRetryCount;

    /**
     * 调度-时间
     */
    private LocalDateTime triggerTime;

    /**
     * 调度-结果码
     */
    private Integer triggerCode;

    /**
     * 调度-结果信息
     */
    private String triggerMsg;

    /**
     * 执行-结果码
     */
    private Integer execCode;

    /**
     * 执行-结果信息
     */
    private String execMsg;

    /**
     * 执行-开始时间
     */
    private LocalDateTime execBeginTime;

    /**
     * 执行-结束时间
     */
    private LocalDateTime execEndTime;

    /**
     * 执行-耗时
     */
    private Integer execCostTime;

    /**
     * 告警状态：0-默认、1-无需告警、2-告警成功、3-告警失败
     */
    private Integer alarmStatus;


}
