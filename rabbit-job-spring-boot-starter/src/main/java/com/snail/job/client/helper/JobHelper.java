package com.snail.job.client.helper;

import com.snail.job.common.model.ResultT;

/**
 * 获取参数
 * 设置执行结果
 * @author WuQinglong
 */
public class JobHelper {

    private static final InheritableThreadLocal<JobContext> CONTEXT_HOLDER = new InheritableThreadLocal<>();

    public static String getJobParam() {
        return CONTEXT_HOLDER.get().getJobParam();
    }

    public static Integer getShardIndex() {
        return CONTEXT_HOLDER.get().getShardIndex();
    }

    public static Integer getShardTotal() {
        return CONTEXT_HOLDER.get().getShardTotal();
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
        JobContext context = CONTEXT_HOLDER.get();
        context.setHandleCode(retCode);
        context.setHandleMsg(retMsg);
    }

    public static void setContext(JobContext context) {
        CONTEXT_HOLDER.set(context);
    }

    public static JobContext getContext() {
        return CONTEXT_HOLDER.get();
    }

    public static void removeContent() {
        CONTEXT_HOLDER.remove();
    }

}
