package com.alsritter.oauth2_2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * @author alsritter
 * @version 1.0
 **/
@SpringBootApplication
@EnableFeignClients("com.alsritter.*") // @EnableFeignClients 默认扫描的范围是启动类及启动以下的包，因此还必须指定 @EnableFeignClients 的 basePackages 属性才行
@EnableRedisHttpSession
@EnableDiscoveryClient
public class OAuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(OAuthApplication.class, args);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
