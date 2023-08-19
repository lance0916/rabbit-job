package com.example.client.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author WuQinglong
 */
@Configuration
@ComponentScan(
        basePackages = "com.snail.job.client.controller",
        nameGenerator = JobBeanNameGenerator.class
)
public class WebConfig implements WebMvcConfigurer {
}
