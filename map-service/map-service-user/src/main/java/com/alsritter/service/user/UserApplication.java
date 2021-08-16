package com.alsritter.service.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * <p>
 * Application
 * </p>
 *
 * @author alsritter
 * @description auto generator
 * @since 2021-06-05 17:00:11
 **/
@EnableDiscoveryClient
@SpringBootApplication
@EnableRedisHttpSession
@EnableCaching
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }
}
