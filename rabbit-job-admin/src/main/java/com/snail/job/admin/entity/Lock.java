package com.snail.job.admin.entity;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author WuQinglong
 * @since 2021-12-06
 */
public class Lock implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * lockKey
     */
    private String lockKey;

    public String getLockKey() {
        return lockKey;
    }

    public void setLockKey(String lockKey) {
        this.lockKey = lockKey;
    }

    @Override
    public String toString() {
        return "Lock{" +
            "lockKey=" + lockKey +
        "}";
    }
}
