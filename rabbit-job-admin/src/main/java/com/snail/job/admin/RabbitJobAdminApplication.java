package com.snail.job.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author WuQinglong
 * @date 2021/9/5 6:19 下午
 */
@SpringBootApplication
public class RabbitJobAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(RabbitJobAdminApplication.class, args);
    }

}
