package com.snail.job.client.config;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

/**
 * 自定义BeanName
 * 加上 rabbitJob 前缀
 * @author WuQinglong created on 2021/11/27 17:42
 */
public class JobBeanNameGenerator implements BeanNameGenerator {

    @Override
    @NonNull
    public String generateBeanName(BeanDefinition definition, @NonNull BeanDefinitionRegistry registry) {
        String beanClassName = definition.getBeanClassName();
        Assert.state(beanClassName != null, "No bean class name set");
        String shortClassName = ClassUtils.getShortName(beanClassName);
        return "rabbitJob" + shortClassName;
    }

}
