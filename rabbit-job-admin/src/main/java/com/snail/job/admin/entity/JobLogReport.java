package com.snail.job.admin.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDate;

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
public class JobLogReport {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 调度日期
     */
    @Column
    private LocalDate triggerDate;

    /**
     * 调度次数
     */
    @Column
    private Integer runningCount;

    /**
     * 调度成功次数
     */
    @Column
    private Integer successCount;

    /**
     * 调度失败次数
     */
    @Column
    private Integer failCount;

}