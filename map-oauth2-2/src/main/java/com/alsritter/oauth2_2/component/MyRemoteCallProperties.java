package com.alsritter.oauth2_2.component;

import cn.hutool.core.codec.Base64;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * 设置一些自定义的 Feign 属性
 *
 * @author alsritter
 * @version 1.0
 **/
@EqualsAndHashCode(callSuper = false)
@Configuration
@EnableConfigurationProperties({MyRemoteCallProperties.ClientIterior.class, MyRemoteCallProperties.ClientWeb.class})
public class MyRemoteCallProperties {
    @Data
    @ConfigurationProperties(prefix = "myremote.client.interior")
    public static class ClientIterior {
        private String clientId;
        private String clientSecret;

        private String base64;

        @PostConstruct
        public void initData() {
            if (clientId != null && clientSecret != null) {
                this.base64 = Base64.encode(clientId + ":" + clientSecret);
            }
        }
    }

    @Data
    @ConfigurationProperties(prefix = "myremote.client.web")
    public static class ClientWeb {
        private String clientId;
        private String clientSecret;

        private String base64;

        @PostConstruct
        public void initData() {
            if (clientId != null && clientSecret != null) {
                this.base64 = Base64.encode(clientId + ":" + clientSecret);
            }
        }
    }
}
