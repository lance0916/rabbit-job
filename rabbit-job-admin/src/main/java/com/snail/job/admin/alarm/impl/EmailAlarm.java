package com.snail.job.admin.alarm.impl;

import com.snail.job.admin.alarm.JobAlarm;
import com.snail.job.admin.model.JobInfo;
import com.snail.job.admin.model.JobLog;
import org.springframework.stereotype.Component;

/**
 * TODO 邮件报警
 * @author 吴庆龙
 */
@Component
public class EmailAlarm implements JobAlarm {

    @Override
    public void doAlarm(JobInfo info, JobLog log) {
    }

}
