package com.snail.job.admin.thread;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.snail.job.admin.model.JobLog;
import com.snail.job.admin.model.JobLogReport;
import com.snail.job.admin.service.JobLogReportService;
import com.snail.job.admin.service.JobLogService;
import com.snail.job.common.thread.RabbitJobAbstractThread;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;
import static com.snail.job.admin.constant.AdminConstants.JOB_REPORT_INTERVAL;

/**
 * 统计每天的任务执行结果数量
 * @author WuQinglong
 */
@Component
public class JobLogReportThread extends RabbitJobAbstractThread {

    @Resource
    private JobLogService jobLogService;
    @Resource
    private JobLogReportService jobLogReportService;

    @Override
    protected void execute() throws InterruptedException {
        long startMillis = System.currentTimeMillis();

        // 当前日期
        Date curDateTime = new Date();
        DateTime beginDate = DateUtil.beginOfDay(curDateTime);
        DateTime endDate = DateUtil.endOfDay(curDateTime);

        // 统计当天的任务执行情况
        List<JobLog> todayJobLogs = jobLogService.list(
                Wrappers.<JobLog>query().between(JobLog.TRIGGER_TIME, beginDate, endDate)
        );

        int successCount = 0, failCount = 0, runningCount = 0;
        for (JobLog log : todayJobLogs) {
            Integer triggerCode = log.getTriggerCode();
            Integer execCode = log.getExecCode();
            if (triggerCode == 200 && execCode == 200) {
                successCount++;
            } else if (triggerCode == 200 && execCode == 0) {
                runningCount++;
            } else {
                failCount++;
            }
        }

        // 更新 job_log_report
        JobLogReport jobLogReport = jobLogReportService.getOne(
                Wrappers.<JobLogReport>query().eq(JobLogReport.TRIGGER_DATE, beginDate)
        );
        if (jobLogReport == null) {
            jobLogReport = new JobLogReport();
        }
        jobLogReport.setTriggerDate(beginDate);
        jobLogReport.setRunningCount(runningCount);
        jobLogReport.setSuccessCount(successCount);
        jobLogReport.setFailCount(failCount);
        jobLogReportService.saveOrUpdate(jobLogReport);

        // 每分钟执行一次
        long costMillis = System.currentTimeMillis() - startMillis;
        if (running && costMillis < JOB_REPORT_INTERVAL) {
            Thread.sleep(JOB_REPORT_INTERVAL - costMillis);
        }
    }

    @Override
    protected String getThreadName() {
        return "JobReport";
    }
}
