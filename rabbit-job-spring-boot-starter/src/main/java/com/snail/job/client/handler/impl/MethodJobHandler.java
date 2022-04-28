package com.snail.job.client.handler.impl;

import com.snail.job.client.handler.IJobHandler;

import java.lang.reflect.Method;

/**
 * 基于Java方法的任务调度
 * @author WuQinglong
 */
public class MethodJobHandler extends IJobHandler {

    /**
     * 目标类
     */
    private final Object target;

    /**
     * 目标方法
     */
    private final Method method;

    /**
     * 初始化方法
     */
    private final Method initMethod;

    /**
     * 销毁方法
     */
    private final Method destroyMethod;

    public MethodJobHandler(Object target, Method method, Method initMethod, Method destroyMethod) {
        this.target = target;
        this.method = method;
        this.initMethod = initMethod;
        this.destroyMethod = destroyMethod;
    }

    @Override
    public void init() throws Exception {
        if (initMethod != null) {
            initMethod.invoke(target);
        }
    }

    @Override
    public void execute() throws Exception {
        method.invoke(target);
    }

    @Override
    public void destroy() throws Exception {
        if (destroyMethod != null) {
            destroyMethod.invoke(target);

        }
    }
}
