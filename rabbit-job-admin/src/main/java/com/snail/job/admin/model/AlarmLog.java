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
@Accessors(fluent = true)
@TableName("alarm_log")
public class AlarmLog {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 日志id
     */
    private Long logId;

    /**
     * 任务id
     */
    private Long jobId;

    /**
     * 告警方式
     */
    private String method;

    /**
     * 告警结果
     */
    private Integer ret;

    /**
     * 告警信息
     */
    private String content;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;


}
