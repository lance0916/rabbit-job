package com.snail.job.admin.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author WuQinglong created on 2021/11/28 15:01
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
public class AlarmLog {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 告警对应的任务ID
     */
    @Column
    private Long jobId;

    /**
     * 告警对应的任务日志ID
     */
    @Column
    private Long jobLogId;

    /**
     * 告警结果
     */
    @Column
    private Byte ret;

    /**
     * 创建时间
     */
    @Column
    private LocalDateTime createTime;

}
