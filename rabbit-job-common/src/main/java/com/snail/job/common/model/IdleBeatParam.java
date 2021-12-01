package com.snail.job.common.model;

/**
 * 执行器是否忙碌参数
 * @author 吴庆龙
 * @date 2020/5/25 12:00 下午
 */
public class IdleBeatParam {

    /**
     * 任务ID
     */
    private Long jobId;

    public IdleBeatParam() {
    }

    public IdleBeatParam(Long jobId) {
        this.jobId = jobId;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

}
