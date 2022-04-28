package com.snail.job.common.model;

import java.time.LocalDateTime;

/**
 * 回调任务执行结果
 * @author 吴庆龙
 */
public class CallbackParam {

    /**
     * 日志 ID
     */
    private Long logId;

    /**
     * 执行结果码
     */
    private Integer execCode;

    /**
     * 执行结果消息
     */
    private String execMsg;

    /**
     * 开始执行时间
     */
    private LocalDateTime beginExecTime;

    /**
     * 结束执行时间
     */
    private LocalDateTime endExecTime;

    public CallbackParam() {
    }

    public CallbackParam(Long logId, Integer execCode, String execMsg, LocalDateTime beginExecTime, LocalDateTime endExecTime) {
        this.logId = logId;
        this.execCode = execCode;
        this.execMsg = execMsg;
        this.beginExecTime = beginExecTime;
        this.endExecTime = endExecTime;
    }

    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public Integer getExecCode() {
        return execCode;
    }

    public void setExecCode(Integer execCode) {
        this.execCode = execCode;
    }

    public String getExecMsg() {
        return execMsg;
    }

    public void setExecMsg(String execMsg) {
        this.execMsg = execMsg;
    }

    public LocalDateTime getBeginExecTime() {
        return beginExecTime;
    }

    public void setBeginExecTime(LocalDateTime beginExecTime) {
        this.beginExecTime = beginExecTime;
    }

    public LocalDateTime getEndExecTime() {
        return endExecTime;
    }

    public void setEndExecTime(LocalDateTime endExecTime) {
        this.endExecTime = endExecTime;
    }
}
