package com.snail.job.common.model;

/**
 * 响应封装
 * @author 吴庆龙
 */
public class ResultT<T> {

    /**
     * 成功
     */
    public static final int SUCCESS_CODE = 200;

    /**
     * 通用失败
     */
    public static final int FAIL_CODE = 500;

    /**
     * 任务执行超时
     */
    public static final int TIMEOUT_CODE = 501;

    /**
     * 任务被终止终止
     * 针对于带有超时时间的任务
     */
    public static final int JOB_ABORT_CODE = 502;

    /**
     * 任务还未进行调度
     */
    public static final int JOB_UN_EXEC_CODE = 504;

    /**
     * 签名错误
     */
    public static final int SIGN_ERROR_CODE = 505;

    /**
     * 执行器忙碌
     */
    public static final int EXECUTOR_BUSY = 506;

    /**
     * 网络错误
     */
    public static final int NETWORK_ERROR = 507;

    /**
     * 未知错误
     */
    public static final int UNKONW_ERROR = 508;

    /**
     * 服务已下线
     */
    public static final int SERVICE_DOWN = 600;

    public static final ResultT<String> SUCCESS = new ResultT<>(null);

    public static final ResultT<String> FAIL = new ResultT<>(FAIL_CODE, null);

    public static final ResultT<String> DOWN = new ResultT<>(SERVICE_DOWN, null);

    /**
     * 状态码
     */
    private int code;

    /**
     * 状态消息
     */
    private String msg;

    /**
     * 响应内容
     */
    private T content;

    public ResultT() {
    }

    public ResultT(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResultT(T content) {
        this.code = SUCCESS_CODE;
        this.content = content;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "ResultT{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", content=" + content +
                '}';
    }
}
