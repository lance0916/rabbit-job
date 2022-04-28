package com.snail.job.common.model;

/**
 * 触发任务参数
 * @author 吴庆龙
 */
public class TriggerParam {

    /**
     * 任务 ID
     */
    private Long jobId;

    /**
     * 任务日志 ID
     */
    private Long logId;

    /**
     * 任务对应的执行逻辑的方法名
     */
    private String execHandler;

    /**
     * 执行参数
     */
    private String execParam;

    /**
     * 执行超时时间
     */
    private Integer execTimeout;

    /**
     * 当前分片数
     */
    private int shardIndex;

    /**
     * 分片总数
     */
    private int shardTotal;

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public String getExecHandler() {
        return execHandler;
    }

    public void setExecHandler(String execHandler) {
        this.execHandler = execHandler;
    }

    public String getExecParam() {
        return execParam;
    }

    public void setExecParam(String execParam) {
        this.execParam = execParam;
    }

    public Integer getExecTimeout() {
        return execTimeout;
    }

    public void setExecTimeout(Integer execTimeout) {
        this.execTimeout = execTimeout;
    }

    public int getShardIndex() {
        return shardIndex;
    }

    public void setShardIndex(int shardIndex) {
        this.shardIndex = shardIndex;
    }

    public int getShardTotal() {
        return shardTotal;
    }

    public void setShardTotal(int shardTotal) {
        this.shardTotal = shardTotal;
    }

}
