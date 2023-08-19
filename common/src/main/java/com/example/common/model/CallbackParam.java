package com.example.common.model;

import java.util.Date;

/**
 * 回调任务执行结果
 * @author WuQinglong
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
    private Date beginExecTime;

    /**
     * 结束执行时间
     */
    private Date endExecTime;

    public CallbackParam() {
    }

    public CallbackParam(Long logId, Integer execCode, String execMsg, Date beginExecTime, Date endExecTime) {
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

    public Date getBeginExecTime() {
        return beginExecTime;
    }

    public void setBeginExecTime(Date beginExecTime) {
        this.beginExecTime = beginExecTime;
    }

    public Date getEndExecTime() {
        return endExecTime;
    }

    public void setEndExecTime(Date endExecTime) {
        this.endExecTime = endExecTime;
    }
}
