package com.example.client.controller;

import com.example.client.service.JobClientService;
import com.example.common.aspect.annotation.CheckServiceAvailable;
import com.example.common.aspect.annotation.CheckSign;
import com.example.common.constant.ServiceStatus;
import com.example.common.model.IdleBeatParam;
import com.example.common.model.ResultT;
import com.example.common.model.TriggerParam;
import com.example.common.tools.GsonTool;
import javax.annotation.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 任务调度接口
 * @author WuQinglong
 */
@Controller
@RequestMapping("/rabbit-job")
@CheckSign
@CheckServiceAvailable
public class RabbitJobController {

    /**
     * 执行逻辑handler
     */
    @Resource
    private JobClientService jobClientService;

    /**
     * 心跳监测
     */
    @PostMapping("/beat")
    public ResponseEntity<?> beat() {
        if (ServiceStatus.status == ServiceStatus.Status.STOPPING) {
            return ResponseEntity.ok(new ResultT<>(ResultT.SERVICE_DOWN, "服务准备下线"));
        }
        return convertToResponseEntity(ResultT.SUCCESS);
    }

    /**
     * 忙碌监测
     */
    @PostMapping("/idleBeat")
    public ResponseEntity<?> idleBeat(@RequestBody String bodyJson) {
        IdleBeatParam idleBeatParam = GsonTool.fromJson(bodyJson, IdleBeatParam.class);
        ResultT<String> resultT = jobClientService.idleBeat(idleBeatParam);
        return convertToResponseEntity(resultT);
    }

    /**
     * 运行任务
     */
    @PostMapping("/run")
    public ResponseEntity<?> run(@RequestBody String bodyJson) {
        TriggerParam triggerParam = GsonTool.fromJson(bodyJson, TriggerParam.class);
        ResultT<String> resultT = jobClientService.run(triggerParam);
        return convertToResponseEntity(resultT);
    }

    private ResponseEntity<String> convertToResponseEntity(ResultT<String> resultT) {
        String json = GsonTool.toJson(resultT);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(json);
    }

}
