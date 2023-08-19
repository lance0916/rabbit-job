package com.example.client.helper;

import com.example.common.model.ResultT;

/**
 * 获取参数
 * 设置执行结果
 * @author WuQinglong
 */
public class JobHelper {

    private static final InheritableThreadLocal<JobContext> contextHolder = new InheritableThreadLocal<>();

    public static String getJobParam() {
        return contextHolder.get().getJobParam();
    }

    public static Integer getShardIndex() {
        return contextHolder.get().getShardIndex();
    }

    public static Integer getShardTotal() {
        return contextHolder.get().getShardTotal();
    }

    public static void success() {
        setRetCode(ResultT.SUCCESS_CODE);
    }

    public static void setFail(String retMsg) {
        setRetCode(ResultT.FAIL_CODE, retMsg);
    }

    public static void setFail(int retCode, String retMsg) {
        setRetCode(retCode, retMsg);
    }

    public static void setRetCode(int retCode) {
        setRetCode(retCode, null);
    }

    public static void setRetCode(int retCode, String retMsg) {
        JobContext context = contextHolder.get();
        context.setHandleCode(retCode);
        context.setHandleMsg(retMsg);
    }

    public static void setContext(JobContext context) {
        contextHolder.set(context);
    }

    public static JobContext getContext() {
        return contextHolder.get();
    }

    public static void removeContent() {
        contextHolder.remove();
    }

}
