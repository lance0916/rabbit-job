package com.snail.job.admin.thread;

import com.snail.job.admin.entity.JobLog;
import com.snail.job.admin.entity.JobLogReport;
import com.snail.job.admin.repository.JobLogReportRepository;
import com.snail.job.admin.repository.JobLogRepository;
import com.snail.job.common.thread.RabbitJobAbstractThread;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.snail.job.admin.constant.AdminConstants.JOB_REPORT_INTERVAL;

/**
 * 统计每天的任务执行结果数量
 * @author 吴庆龙
 * @date 2020/7/21 5:05 下午
 */
@Component
public class JobLogReportThread extends RabbitJobAbstractThread {

    @Resource
    private JobLogRepository jobLogRepository;
    @Resource
    private JobLogReportRepository jobLogReportRepository;

    @Override
    protected void doRun() throws InterruptedException {
        long startMillis = System.currentTimeMillis();

        // 当前日期
        LocalDate todayDate = LocalDate.now();
        LocalDateTime beginTime = todayDate.atStartOfDay();
        LocalDateTime endTime = beginTime.plusDays(1);

        // 统计当天的任务执行情况
        List<JobLog> todayJobLogs = jobLogRepository.findAllTodayJobLog(beginTime, endTime);
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
        JobLogReport jobLogReport = jobLogReportRepository.findAllByTriggerDateEquals(todayDate);
        if (jobLogReport == null) {
            jobLogReport = new JobLogReport();
        }
        jobLogReport.setTriggerDate(todayDate);
        jobLogReport.setRunningCount(runningCount);
        jobLogReport.setSuccessCount(successCount);
        jobLogReport.setFailCount(failCount);
        jobLogReportRepository.saveAndFlush(jobLogReport);

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
