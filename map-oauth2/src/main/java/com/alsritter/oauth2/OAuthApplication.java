package com.alsritter.oauth2;

import com.alsritter.oauth2.prop.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 这个配置主要参考自：
 *
 * Spring Cloud OAuth2 实现用户认证和单点登录 - 古时的风筝的文章 - 知乎
 * https://zhuanlan.zhihu.com/p/88421654
 *
 * 在 Gateway 配置鉴权(失败了)
 * https://juejin.cn/post/6850037263707930631#comment
 *
 * 主要的接口
 * POST /oauth/authorize 授权码模式认证授权接口
 * GET/POST /oauth/token 获取 token 的接口
 * POST /oauth/check_token 检查 token 合法性接口
 *
 * @author alsritter
 * @version 1.0
 **/
@EnableConfigurationProperties(RsaKeyProperties.class)
@SpringBootApplication
@EnableFeignClients("com.alsritter.*") // @EnableFeignClients 默认扫描的范围是启动类及启动以下的包，因此还必须指定 @EnableFeignClients 的 basePackages 属性才行
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
