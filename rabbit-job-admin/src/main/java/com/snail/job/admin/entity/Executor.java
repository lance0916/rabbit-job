package com.snail.job.admin.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 执行器注册信息
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
public class Executor {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 关联应用
     */
    @Column
    private String appName;

    /**
     * 执行器地址
     */
    @Column
    private String address;

    /**
     * 删除标志
     */
    @Column
    private Byte deleted;

    /**
     * 创建时间
     */
    @Column
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Column
    private LocalDateTime updateTime;

}
