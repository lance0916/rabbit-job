package com.snail.job.admin.bean.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author WuQinglong created on 2021/12/13 07:52
 */
@Getter
@Setter
@ToString
public class JobInfoQueryRequest extends BaseQueryRequest {

    /**
     * 任务名
     */
    private String name;

    /**
     * 应用名
     */
    private String appName;

    /**
     * 创建人
     */
    private String authorName;

    /**
     * 运行状态
     */
    private Byte triggerStatus;

}
