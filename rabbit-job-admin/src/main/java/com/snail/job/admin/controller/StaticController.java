package com.snail.job.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author 吴庆龙
 * @date 2020/11/26 下午5:58
 */
@Controller
public class StaticController {

    @RequestMapping({"/", "index"})
    public String index() {
        return "index";
    }

    @RequestMapping("/app-page")
    public String appPage() {
        return "app";
    }

    @RequestMapping("/info-page")
    public String infoPage() {
        return "info";
    }

    @RequestMapping("/log-page")
    public String logPage() {
        return "log";
    }

}
