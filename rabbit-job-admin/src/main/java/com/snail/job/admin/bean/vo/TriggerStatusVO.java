package com.snail.job.admin.bean.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author WuQinglong created on 2021/12/18 18:06
 */
@Getter
@Setter
@ToString
public class TriggerStatusVO {

    /**
     * 应用数量
     */
    private long appNum;

    /**
     * 任务数量
     */
    private long jobNum;

    /**
     * 调度次数
     */
    private int triggerNum;

    /**
     * 调度成功次数
     */
    private int triggerSuccessNum;

    /**
     * 调度失败次数
     */
    private int triggerFailNum;

    /**
     * 今日调度次数
     */
    private int todayTriggerNum;

    /**
     * 今日调度成功次数
     */
    private int todayTriggerSuccessNum;

    /**
     * 今日调度失败次数
     */
    private int todayTriggerFailNum;

}
