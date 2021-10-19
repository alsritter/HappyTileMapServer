package com.alsritter.service.forum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * <p>
 * Application
 * </p>
 *
 * @author alsritter
 * @description auto generator
 * @since 2021-08-15 11:30:49
 **/
@EnableFeignClients("com.alsritter.*")
// @EnableFeignClients 默认扫描的范围是启动类及启动以下的包，因此还必须指定 @EnableFeignClients 的 basePackages 属性才行
@EnableRedisHttpSession
@EnableDiscoveryClient
@SpringBootApplication
@EnableScheduling // 开启定时任务
public class ForumApplication {
    public static void main(String[] args) {
        SpringApplication.run(ForumApplication.class, args);
    }
}
