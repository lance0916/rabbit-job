package com.snail.job.admin.alarm.impl;

import com.snail.job.admin.entity.JobInfo;
import com.snail.job.admin.entity.JobLog;
import com.snail.job.admin.alarm.JobAlarm;
import org.springframework.stereotype.Component;

/**
 * TODO 短信报警
 * @author WuQinglong
 * @date 2021/9/6 9:59 下午
 */
@Component
public class SmsAlarm implements JobAlarm {

    @Override
    public boolean doAlarm(JobInfo info, JobLog log) {
        return true;
    }

}
