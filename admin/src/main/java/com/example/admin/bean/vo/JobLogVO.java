package com.example.admin.bean.vo;

import com.example.admin.bean.entity.JobLog;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author WuQinglong
 */
@Getter
@Setter
@ToString
public class JobLogVO extends JobLog {

    private String jobName;

}