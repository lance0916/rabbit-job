package com.snail.job.admin.model;

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
public class Lock {

    /**
     * lockKey
     */
    private String lockKey;


}
