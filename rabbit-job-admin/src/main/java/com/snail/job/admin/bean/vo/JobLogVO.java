package com.snail.job.admin.bean.vo;

import com.snail.job.admin.model.JobLog;
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