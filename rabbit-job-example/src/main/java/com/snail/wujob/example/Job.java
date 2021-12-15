package com.snail.wujob.example;

import com.snail.job.client.annotation.RabbitJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author WuQinglong created on 2021/12/15 07:17
 */
@Slf4j
@Component
public class Job {

    @RabbitJob("eachSecond")
    public void f() {
        log.info("每秒执行一次");
    }

}
