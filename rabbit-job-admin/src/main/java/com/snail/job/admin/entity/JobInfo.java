package com.snail.job.admin.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 任务信息
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
public class JobInfo {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String appName;

    @Column
    private String cron;

    @Column
    private LocalDateTime createTime;

    @Column
    private LocalDateTime updateTime;

    @Column
    private String authorName;

    @Column
    private String authorEmail;

    @Column
    private String execRouteStrategy;

    @Column
    private String execHandler;

    @Column
    private String execParam;

    @Column
    private Integer execTimeout;

    @Column
    private Byte execFailRetryCount;

    @Column
    private Byte triggerStatus;

    @Column
    private Long prevTriggerTime;

    @Column
    private Long nextTriggerTime;

}