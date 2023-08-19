package com.example.admin.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.example.common.RepeatReadBodyFilter;
import java.util.Collections;
import java.util.List;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author WuQinglong
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * 自定义消息转换器
     * 忽略 SpringMVC 默认的消息转换器
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new ByteArrayHttpMessageConverter());
        converters.add(new StringHttpMessageConverter());
        converters.add(new AllEncompassingFormHttpMessageConverter());

        ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json()
                .applicationContext(applicationContext)
                .failOnUnknownProperties(false)
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
                .build();
        converters.add(new MappingJackson2HttpMessageConverter(objectMapper));
    }

    /**
     * 支持 body 体可重读读
     */
    @Bean
    public FilterRegistrationBean<RepeatReadBodyFilter> repeatReadBodyFilter() {
        FilterRegistrationBean<RepeatReadBodyFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new RepeatReadBodyFilter());
        bean.setName("repeatReadBody");
        bean.setUrlPatterns(Collections.singletonList("/*"));
        return bean;
    }

}
