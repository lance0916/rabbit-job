package com.snail.job.client.config;

import com.snail.job.common.exception.NoAdminAddressException;
import com.snail.job.common.proxy.AdminProxy;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author WuQinglong
 */
@Configuration
@ComponentScan(
        basePackages = {
                "com.snail.job.client.biz",
                "com.snail.job.client.service",
                "com.snail.job.client.thread",
                "com.snail.job.client.listener",
        },
        basePackageClasses = JobAutoConfiguration.class,
        nameGenerator = JobBeanNameGenerator.class
)
@EnableConfigurationProperties(RabbitJobProperties.class)
public class JobAutoConfiguration {

    /**
     * 调度中心配置
     */
    @Bean(name = "adminProxies")
    public List<AdminProxy> adminBizClients(RabbitJobProperties rabbitJobProperties) {
        List<String> addresses = rabbitJobProperties.getAdminAddresses();
        if (addresses == null || addresses.isEmpty()) {
            throw new NoAdminAddressException("未配置调度中心");
        }

        String secretKey = rabbitJobProperties.getSecretKey();

        List<AdminProxy> proxies = new ArrayList<>(addresses.size());
        for (String address : addresses) {
            proxies.add(new AdminProxy(address, secretKey));
        }
        return proxies;
    }

}
