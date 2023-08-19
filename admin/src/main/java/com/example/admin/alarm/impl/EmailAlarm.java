package com.example.admin.alarm.impl;

import com.example.admin.bean.entity.JobInfo;
import com.example.admin.bean.entity.JobLog;
import com.example.admin.alarm.JobAlarm;
import org.springframework.stereotype.Component;

/**
 * TODO 邮件报警
 * @author WuQinglong
 */
@Component
public class EmailAlarm implements JobAlarm {

    @Override
    public void doAlarm(JobInfo info, JobLog log) {
    }

}
