package com.snail.job.admin.alarm.impl;

import com.snail.job.admin.entity.JobInfo;
import com.snail.job.admin.entity.JobLog;
import com.snail.job.admin.alarm.JobAlarm;
import org.springframework.stereotype.Component;

/**
 * TODO WebHook回调
 * @author WuQinglong
 * @date 2021/9/7 1:46 下午
 */
@Component
public class WebHookAlarm implements JobAlarm {
    @Override
    public boolean doAlarm(JobInfo jobInfo, JobLog jobLog) {
        return false;
    }
}
