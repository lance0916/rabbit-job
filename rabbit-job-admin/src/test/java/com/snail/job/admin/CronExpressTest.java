package com.snail.job.admin;

import cn.hutool.core.date.DateUtil;
import org.springframework.scheduling.support.CronExpression;

import java.time.LocalDateTime;

/**
 * @author WuQinglong created on 2021/12/15 07:47
 */
public class CronExpressTest {

    public static void main(String[] args) {
        CronExpression cronExpression = CronExpression.parse("0 0 0/1 * * ?");
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime next = cronExpression.next(localDateTime);
        System.out.println(DateUtil.formatLocalDateTime(next));
    }

}
