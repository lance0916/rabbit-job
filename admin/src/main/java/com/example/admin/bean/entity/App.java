package com.example.admin.bean.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 * @author WuQinglong
 * @since 2021-12-06
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("app")
public class App extends Model<App> {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 应用名称
     */
    @TableField("name")
    private String name;

    /**
     * 描述
     */
    @TableField("title")
    private String title;

    /**
     * 注册类型。自动注册=0；手动注册=1
     */
    @TableField("type")
    private Integer type;

    /**
     * 是否删除
     */
    @TableField("deleted")
    private Integer deleted;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;

    public static final String ID = "id";

    public static final String NAME = "name";

    public static final String TITLE = "title";

    public static final String TYPE = "type";

    public static final String DELETED = "deleted";

    public static final String CREATE_TIME = "create_time";

    public static final String UPDATE_TIME = "update_time";

    @Override
    public Serializable pkVal() {
        return this.id;
    }

}
