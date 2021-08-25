package com.alsritter.oauth2_2.component;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 设置一些自定义的 Feign 属性
 *
 * @author alsritter
 * @version 1.0
 **/
@EqualsAndHashCode(callSuper = false)
@Configuration
@EnableConfigurationProperties({MyFeignProperties.ClientSecurity.class})
public class MyFeignProperties {

    /**
     * 这个 Token 是共享的
     */
    public static String token = "";

    @Data
    @ConfigurationProperties(prefix = "myfeign.security")
    public static class ClientSecurity {
        private String clientId;
        private String clientSecret;
    }
}
