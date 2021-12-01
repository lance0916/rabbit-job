package com.snail.wujob.example;

import org.springframework.beans.BeansException;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author WuQinglong
 */
@SpringBootApplication
public class ExampleApplication implements ApplicationRunner, ApplicationContextAware {

    private ApplicationContext applicationContext;

    public static void main(String[] args) {
        SpringApplication.run(ExampleApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String[] names = applicationContext.getBeanDefinitionNames();
        for (String name : names) {
            System.out.println(name);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
