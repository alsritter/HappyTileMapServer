package com.alsritter.mapuaa;

import com.alsritter.mapuaa.config.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

/**
 * <p>
 * Application
 * </p>
 *
 * @author alsritter
 * @description 认证中心
 * @since 2021-06-02 00:37:39
 **/
@SpringBootApplication
@EnableConfigurationProperties(RsaKeyProperties.class)                    // 将配置类放入 Spring 容器中
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true) // 开启鉴权注解
public class OAuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(OAuthApplication.class, args);
    }
}
