package com.alsritter.oauth2_2.component;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import feign.Logger;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;

/**
 * 添上 Feign 的请求认证
 *
 * @author alsritter
 * @version 1.0
 **/
@Configuration
@RequiredArgsConstructor
// public class FeignRequestConfig implements RequestInterceptor {
public class FeignRequestConfig {
    /**
     * 打印 feign 请求日志级别
     */
    @Bean
    public Logger.Level level() {
        return Logger.Level.FULL;
    }

    // private final MyFeignProperties.ClientSecurity myFeignProperties;
    // private RestTemplate restTemplate;
    //
    // @PostConstruct
    // public void initData() {
    //     this.restTemplate = restTemplate();
    // }
    //
    //
    // public RestTemplate restTemplate() {
    //     RestTemplate template = new RestTemplate(
    //             new HttpComponentsClientHttpRequestFactory()); // 使用HttpClient，支持GZIP
    //     template.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8)); // 支持中文编码
    //     return template;
    // }
    //
    // public String loadToken() {
    //     HttpHeaders headers = new HttpHeaders();
    //     headers.add("Content-Type", "application/x-www-form-urlencoded");
    //     // 设置请求参数
    //     MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();
    //     postParameters.add("grant_type", "client_credentials");
    //     postParameters.add("client_id", myFeignProperties.getClientId());
    //     postParameters.add("client_secret", myFeignProperties.getClientSecret());
    //     HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(postParameters, headers);
    //     ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://localhost:38094/oauth/token", httpEntity, String.class);
    //     String body = responseEntity.getBody();
    //     JSONObject jsonObject = JSONUtil.parseObj(body);
    //     String accessToken = jsonObject.getStr("access_token");
    //     MyFeignProperties.token = accessToken;
    //     return accessToken;
    // }


    // @Override
    // public void apply(RequestTemplate template) {
    //     ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    //     if (attributes == null) return;
    //     HttpServletRequest request = attributes.getRequest();
    //     Enumeration<String> headerNames = request.getHeaderNames();
    //
    //     if (headerNames == null) return;
    //     //处理上游请求头信息，传递时继续携带
    //     while (headerNames.hasMoreElements()) {
    //         String name = headerNames.nextElement();
    //         String values = request.getHeader(name);
    //         template.header(name, values);
    //     }
    //
    //     String token = MyFeignProperties.token;
    //     if (StringUtil.isNullOrEmpty(token)) {
    //         token = loadToken();
    //     }
    //
    //     template.header("Authorization", "Bearer " + token);
    // }

}
