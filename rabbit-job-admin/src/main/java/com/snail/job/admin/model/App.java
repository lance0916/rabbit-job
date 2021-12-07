package com.snail.job.admin.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author WuQinglong
 * @since 2021-12-06
 */
@Getter
@Setter
@Accessors(chain = true)
public class App {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 应用名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 注册类型。自动注册=0；手动注册=1
     */
    private Integer type;

    /**
     * 执行器地址
     */
    private String addresses;

    /**
     * 是否删除
     */
    private Integer deleted;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
