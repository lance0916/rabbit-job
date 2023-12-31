package com.example.admin.bean.req;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author WuQinglong
 * @since 2021/12/13 6:12 下午
 */
@Getter
@Setter
@ToString
public class JobLogQueryReq extends BaseQueryReq {

    /**
     * 应用名
     */
    private String appName;

    /**
     * 任务Id
     */
    private Integer jobId;

    /**
     * 调度码
     */
    private Integer triggerCode;

    /**
     * 调度开始时间
     */
    private LocalDate triggerBeginDate;

    /**
     * 调度结束时间
     */
    private LocalDate triggerEndDate;

    /**
     * 执行码
     */
    private Integer execCode;

}
