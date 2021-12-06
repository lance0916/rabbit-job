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
@TableName("alarm_log")
public class AlarmLog {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 日志id
     */
    private Integer logId;

    /**
     * 任务id
     */
    private Integer jobId;

    /**
     * 告警方式
     */
    private String method;

    /**
     * 告警信息
     */
    private String content;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;


}
