package com.snail.job.client.controller;

import com.snail.job.common.model.IdleBeatParam;
import com.snail.job.common.model.ResultT;
import com.snail.job.common.model.TriggerParam;
import com.snail.job.common.tools.GsonTool;
import com.snail.job.client.service.JobClientService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 * 任务调度接口
 * TODO 加入验签逻辑
 * @author WuQinglong created on 2021/11/30 6:51 下午
 */
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
        String json = GsonTool.toJson(ResultT.SUCCESS);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(json);
    }

    /**
     * 忙碌监测
     */
    @PostMapping("/idleBeat")
    public ResponseEntity<?> idleBeat(@RequestBody String bodyJson) {
        IdleBeatParam idleBeatParam = GsonTool.fromJson(bodyJson, IdleBeatParam.class);
        ResultT<String> resultT = jobClientService.idleBeat(idleBeatParam);
        String json = GsonTool.toJson(resultT);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(json);
    }

    /**
     * 运行任务
     */
    @GetMapping("/run")
    public ResponseEntity<?> run(@RequestBody String bodyJson) {
        TriggerParam triggerParam = GsonTool.fromJson(bodyJson, TriggerParam.class);
        ResultT<String> resultT = jobClientService.run(triggerParam);
        String json = GsonTool.toJson(resultT);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(json);
    }

}
