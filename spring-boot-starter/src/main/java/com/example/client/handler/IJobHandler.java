package com.example.client.handler;

/**
 * 任务调度接口
 * @author WuQinglong
 */
public abstract class IJobHandler {

    /**
     * 任务初始化方法
     */
    public void init() throws Exception {
    }

    /**
     * 具体的执行逻辑
     * @exception Exception 执行异常
     */
    public abstract void execute() throws Exception;

    /**
     * 任务销毁方法
     */
    public void destroy() throws Exception {
    }

}
