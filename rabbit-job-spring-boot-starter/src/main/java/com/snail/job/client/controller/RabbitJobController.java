package com.snail.job.client.controller;

import com.snail.job.client.service.JobClientService;
import com.snail.job.common.annotation.CheckSign;
import com.snail.job.common.model.IdleBeatParam;
import com.snail.job.common.model.ResultT;
import com.snail.job.common.model.TriggerParam;
import com.snail.job.common.tools.GsonTool;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 * 任务调度接口
 * @author WuQinglong created on 2021/11/30 6:51 下午
 */
@CheckSign
@Controller
@RequestMapping("/rabbit-job")
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
