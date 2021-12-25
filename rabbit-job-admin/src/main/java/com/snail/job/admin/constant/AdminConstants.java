package com.snail.job.admin.constant;

/**
 * @author 吴庆龙
 * @date 2020/8/1 2:28 下午
 */
public interface AdminConstants {

    /**
     * 任务执行间隔时间
     */
    long SCAN_JOB_SLEEP_MS = 10000;

    /**
     * 失败任务监控线程执行间隔时间
     */
    int FAIL_MONITOR_INTERVAL = 1000;

    /**
     * 任务报告统计线程执行间隔时间
     */
    int JOB_REPORT_INTERVAL = 1000 * 60;

    /**
     * 任务扫描线程执行间隔时间
     */
    int JOB_SCHEDULE_INTERVAL = 1000 * 3;

    /**
     * 任务扫描预扫描时间
     */
    int JOB_PRE_SCAN_TIME = 5;

    /**
     * 任务调度线程执行间隔时间
     */
    int JOB_TRIGGER_INTERVAL = 1000;

}
