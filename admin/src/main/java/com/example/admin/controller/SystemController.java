package com.example.admin.controller;

import cn.hutool.system.JavaRuntimeInfo;
import cn.hutool.system.JvmInfo;
import cn.hutool.system.RuntimeInfo;
import cn.hutool.system.oshi.CpuInfo;
import cn.hutool.system.oshi.OshiUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.admin.bean.vo.TriggerStatusVO;
import com.example.admin.bean.entity.App;
import com.example.admin.bean.entity.JobInfo;
import com.example.admin.bean.entity.JobLogReport;
import com.example.admin.service.AppService;
import com.example.admin.service.JobInfoService;
import com.example.admin.service.JobLogReportService;
import com.example.common.model.ResultT;
import java.time.LocalDate;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import oshi.hardware.GlobalMemory;
import oshi.software.os.OSProcess;

/**
 * @author WuQinglong created on 2021/12/18 16:10
 */
@RestController
@RequestMapping("/system")
public class SystemController {

    @Resource
    private AppService appService;
    @Resource
    private JobInfoService jobInfoService;
    @Resource
    private JobLogReportService jobLogReportService;

    /**
     * 任务运行情况
     */
    @GetMapping("/status/trigger")
    public ResultT<?> triggerStatus() {
        long appCount = appService.count(Wrappers.<App>lambdaQuery().eq(App::getDeleted, 0));
        long jobCount = jobInfoService.count(Wrappers.<JobInfo>lambdaQuery().eq(JobInfo::getDeleted, 0));

        int triggerNum = 0;
        int triggerSuccessNum = 0;
        int triggerFailNum = 0;
        int todayTriggerNum = 0;
        int todayTriggerSuccessNum = 0;
        int todayTriggerFailNum = 0;
        LocalDate nowDate = LocalDate.now();
        List<JobLogReport> list = jobLogReportService.list();
        for (JobLogReport report : list) {
            triggerNum++;
            triggerSuccessNum += report.getSuccessCount();
            triggerFailNum += report.getFailCount();

            // 今日
            if (nowDate.equals(report.getTriggerDate())) {
                todayTriggerNum++;
                todayTriggerSuccessNum += report.getSuccessCount();
                todayTriggerFailNum += report.getFailCount();
            }
        }

        TriggerStatusVO vo = new TriggerStatusVO();
        vo.setAppNum(appCount);
        vo.setJobNum(jobCount);
        vo.setTriggerNum(triggerNum);
        vo.setTriggerSuccessNum(triggerSuccessNum);
        vo.setTriggerFailNum(triggerFailNum);
        vo.setTodayTriggerNum(todayTriggerNum);
        vo.setTodayTriggerSuccessNum(todayTriggerSuccessNum);
        vo.setTodayTriggerFailNum(todayTriggerFailNum);
        return new ResultT<>(vo);
    }

    /**
     * CPU 使用率
     */
    @GetMapping("/status/cpu")
    public ResultT<?> statusCpu() {
        CpuInfo info = OshiUtil.getCpuInfo();
        OSProcess process = OshiUtil.getCurrentProcess();

        StringBuilder sb = new StringBuilder();
        sb.append("CPU Num      :").append(info.getCpuNum()).append("<br>");
        sb.append("CPU Sys      :").append(info.getSys()).append("<br>");
        sb.append("CPU User     :").append(info.getUsed()).append("<br>");
        sb.append("CPU Free     :").append(info.getFree()).append("<br>");
        sb.append("ProcessId    :").append(process.getProcessID()).append("<br>");
        sb.append("StartTime    :").append(process.getStartTime()).append("<br>");
        sb.append("UpTime       :").append(process.getUpTime()).append("<br>");
        return new ResultT<>(sb.toString());
    }

    /**
     * 内存使用率
     */
    @GetMapping("/status/memory")
    public ResultT<?> statusMemory() {
        GlobalMemory memory = OshiUtil.getMemory();
        RuntimeInfo runtimeInfo = new RuntimeInfo();

        StringBuilder sb = new StringBuilder();
        sb.append("Memory Physics Total     :").append(memory.getTotal()).append("<br>");
        sb.append("Memory Physics Available :").append(memory.getAvailable()).append("<br>");
        sb.append("Memory Physics PageSize  :").append(memory.getPageSize()).append("<br>");
        // 获得JVM最大内存
        sb.append("Memory JVM Max           :").append(runtimeInfo.getMaxMemory()).append("<br>");
        // 获得JVM已分配内存
        sb.append("Memory JVM Total         :").append(runtimeInfo.getTotalMemory()).append("<br>");
        // 获得JVM已分配内存中的剩余空间
        sb.append("Memory JVM Free          :").append(runtimeInfo.getFreeMemory()).append("<br>");
        // 获得JVM最大可用内存
        sb.append("Memory JVM Usable        :").append(runtimeInfo.getUsableMemory()).append("<br>");
        return new ResultT<>(sb.toString());
    }

    /**
     * Java 信息
     */
    @GetMapping("/status/java")
    public ResultT<?> statusJava() {
        JavaRuntimeInfo javaRuntimeInfo = new JavaRuntimeInfo();
        JvmInfo jvmInfo = new JvmInfo();

        StringBuilder sb = new StringBuilder();
        sb.append("JavaVM Name              :").append(jvmInfo.getName()).append("<br>");
        sb.append("JavaVM Version           :").append(jvmInfo.getVersion()).append("<br>");
        sb.append("JavaVM Vendor            :").append(jvmInfo.getVendor()).append("<br>");
        sb.append("JavaVM Info              :").append(jvmInfo.getInfo()).append("<br>");
        sb.append("Java Runtime Version     :").append(javaRuntimeInfo.getName()).append("<br>");
        sb.append("Java Home Dir            :").append(javaRuntimeInfo.getHomeDir()).append("<br>");
        return new ResultT<>(sb.toString());
    }

}
