package com.snail.job.admin.controller;

import com.snail.job.admin.service.trigger.JobAdminService;
import com.snail.job.common.model.CallbackParam;
import com.snail.job.common.model.RegistryParam;
import com.snail.job.common.model.ResultT;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 接收 Worker 节点的请求
 * @author 吴庆龙
 * @date 2020/6/3 11:31 上午
 */
@RestController
@RequestMapping("/api")
public class ApiController {

    @Resource
    private JobAdminService jobAdminService;

    @PostMapping("/beat")
    public ResultT<String> beat() {
        return jobAdminService.beat();
    }

    @PostMapping("/registry")
    public ResultT<String> registry(@RequestBody RegistryParam registryParam) {
        return jobAdminService.registry(registryParam);
    }

    @PostMapping("/remove")
    public ResultT<String> remove(@RequestBody RegistryParam registryParam) {
        return jobAdminService.remove(registryParam);
    }

    @PostMapping("/callback")
    public ResultT<String> callback(@RequestBody List<CallbackParam> list) {
        return jobAdminService.callback(list);
    }

}
