package com.snail.job.admin.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

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
@TableName("lock")
public class Lock extends Model<Lock> {

    /**
     * lockKey
     */
    @TableField("lock_key")
    private String lockKey;


    public static final String LOCK_KEY = "lock_key";

    @Override
    public Serializable pkVal() {
        return null;
    }

}
