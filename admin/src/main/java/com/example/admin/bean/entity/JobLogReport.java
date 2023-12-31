package com.example.admin.bean.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

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
@TableName("job_log_report")
public class JobLogReport extends Model<JobLogReport> {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 调度日期
     */
    @TableField("trigger_date")
    private Date triggerDate;

    /**
     * 运行中-日志数量
     */
    @TableField("running_count")
    private Integer runningCount;

    /**
     * 执行成功-日志数量
     */
    @TableField("success_count")
    private Integer successCount;

    /**
     * 执行失败-日志数量
     */
    @TableField("fail_count")
    private Integer failCount;

    public static final String ID = "id";

    public static final String TRIGGER_DATE = "trigger_date";

    public static final String RUNNING_COUNT = "running_count";

    public static final String SUCCESS_COUNT = "success_count";

    public static final String FAIL_COUNT = "fail_count";

    @Override
    public Serializable pkVal() {
        return this.id;
    }

}
