package com.snail.job.admin.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
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
public class Application {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 应用名
     */
    @Column
    private String name;

    /**
     * 标题
     */
    @Column
    private String title;

    /**
     * 应用中执行器的注册类型
     */
    @Column
    private Byte type;

    /**
     * 应用对应的地址
     */
    @Column
    private String addresses;

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