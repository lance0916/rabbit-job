package com.snail.job.admin.alarm.impl;

import com.snail.job.admin.alarm.JobAlarm;
import com.snail.job.admin.model.JobInfo;
import com.snail.job.admin.model.JobLog;
import org.springframework.stereotype.Component;

/**
 * TODO 邮件报警
 * @author 吴庆龙
 * @date 2020/7/20 5:43 下午
 */
@Component
public class EmailAlarm implements JobAlarm {

    @Override
    public void doAlarm(JobInfo info, JobLog log) {
    }

}
