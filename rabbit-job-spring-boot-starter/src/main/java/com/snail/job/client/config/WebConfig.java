package com.snail.job.client.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author WuQinglong created on 2021/12/19 20:39
 */
@Configuration
@ComponentScan(
        basePackages = "com.snail.job.client.controller"
        , nameGenerator = JobBeanNameGenerator.class
)
public class WebConfig implements WebMvcConfigurer {
}
