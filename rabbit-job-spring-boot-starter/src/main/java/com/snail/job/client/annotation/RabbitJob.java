package com.snail.job.client.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 任务注解
 * @author WuQinglong
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RabbitJob {

    /**
     * 任务名
     */
    String name();

    /**
     * 任务的初始化方法
     * 要求是本类中的方法名
     */
    String init() default "";

    /**
     * 任务的销毁方法
     * 要求是本类中的方法名
     */
    String destroy() default "";

}
