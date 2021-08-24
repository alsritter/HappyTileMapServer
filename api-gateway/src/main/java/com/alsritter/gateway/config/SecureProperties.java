package com.alsritter.gateway.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 网关白名单配置
 *
 * @author alsritter
 * @version 1.0
 **/

@EqualsAndHashCode(callSuper = false)
@Configuration
@EnableConfigurationProperties({SecureProperties.IgnoreUrl.class, SecureProperties.TokenConfig.class})
public class SecureProperties {

    @Data
    @ConfigurationProperties(prefix = "secure.ignore")
    public static class IgnoreUrl {
        private List<String> urls;
    }

    @Data
    @ConfigurationProperties(prefix = "secure.config")
    public static class TokenConfig {
        private String clientId;
        private String clientSecret;
        private String checkTokenEndpointUrl;
    }
}
