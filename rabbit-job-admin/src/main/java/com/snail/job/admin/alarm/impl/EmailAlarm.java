package com.snail.job.admin.alarm.impl;

import com.snail.job.admin.entity.JobInfo;
import com.snail.job.admin.entity.JobLog;
import com.snail.job.admin.alarm.JobAlarm;
import org.springframework.stereotype.Component;

/**
 * TODO 邮件报警
 * @author 吴庆龙
 * @date 2020/7/20 5:43 下午
 */
@Component
public class EmailAlarm implements JobAlarm {

    @Override
    public boolean doAlarm(JobInfo info, JobLog log) {
        return true;
    }

}
