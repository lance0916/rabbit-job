package com.snail.job.admin.alarm;

import com.snail.job.admin.model.JobInfo;
import com.snail.job.admin.model.JobLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 定义报警
 * @author 吴庆龙
 * @date 2020/7/20 5:25 下午
 */
public interface JobAlarm {

    Logger log = LoggerFactory.getLogger(JobAlarm.class);

    /**
     * 报警方法
     * @param jobInfo 任务信息类
     * @param jobLog  执行日志类
     */
    void doAlarm(JobInfo jobInfo, JobLog jobLog);

}
