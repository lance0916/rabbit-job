package com.snail.job.client.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 执行器配置
 * @author WuQinglong
 */
@ConfigurationProperties(prefix = "rabbit-job")
@Validated
public class JobClientProperties {

    /**
     * 执行器配置
     */
    @Valid
    private Executor executor;

    /**
     * 可配置多个调度中心
     */
    @NotEmpty
    private List<String> adminAddresses;

    /**
     * 加签密钥
     */
    @NotEmpty
    private String secretKey;

    public Executor getExecutor() {
        return executor;
    }

    public void setExecutor(Executor executor) {
        this.executor = executor;
    }

    public List<String> getAdminAddresses() {
        return adminAddresses;
    }

    public void setAdminAddresses(List<String> adminAddresses) {
        this.adminAddresses = adminAddresses;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    /**
     * 执行器属性
     */
    public static class Executor {

        /**
         * 执行器注册的应用名称
         */
        @NotEmpty
        private String appName;

        /**
         * 执行器的外网域名
         * Nginx 必须解析到指定的端口上（port参数）
         */
        private String address;

        /**
         * 执行器的外网ip
         */
        private String ip;

        public String getAppName() {
            return appName;
        }

        public void setAppName(String appName) {
            this.appName = appName;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

    }

}
