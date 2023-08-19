package com.example.job;

import com.example.client.annotation.RabbitJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author WuQinglong created on 2021/12/15 07:17
 */
@Slf4j
@Component
public class Job {

    @RabbitJob(name = "eachSecond")
    public void f() {
        log.info("每秒执行一次");
    }

}
