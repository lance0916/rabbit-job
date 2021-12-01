package com.snail.job.admin.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 任务日志
 * @author WuQinglong created on 2021/11/27 20:13
 */
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
@DynamicInsert
@DynamicUpdate
public class JobLog {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 任务的ID
     */
    @Column
    private Long jobId;

    /**
     * 应用名
     */
    @Column
    private String appName;

    /**
     * 创建时间
     */
    @Column
    private LocalDateTime createTime;

    /**
     * 执行地址
     */
    @Column
    private String execAddress;

    /**
     * 执行方法
     */
    @Column
    private String execHandler;

    /**
     * 执行参数
     */
    @Column
    private String execParam;

    /**
     * 失败重试次数
     */
    @Column
    private Byte failRetryCount;

    /**
     * 触发时间
     */
    @Column
    private LocalDateTime triggerTime;

    /**
     * 触发码
     */
    @Column
    private Integer triggerCode;

    /**
     * 触发类型
     */
    @Column
    private String triggerType;

    /**
     * 触发信息
     */
    @Column
    private String triggerMsg;

    /**
     * 执行码
     */
    @Column
    private Integer execCode;

    /**
     * 执行信息
     */
    @Column
    private String execMsg;

    /**
     * 执行开始时间
     */
    @Column
    private LocalDateTime execBeginTime;

    /**
     * 执行结束时间
     */
    @Column
    private LocalDateTime execEndTime;

    /**
     * 执行耗时
     */
    @Column
    private Integer execCostTime;

    /**
     * 告警状态
     */
    @Column
    private Byte alarmStatus;

}