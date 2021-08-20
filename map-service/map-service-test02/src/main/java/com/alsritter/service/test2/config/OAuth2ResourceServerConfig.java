package com.alsritter.service.test2.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 参考资料 Spring Security 与 OAuth2（资源服务器）
 * https://www.jianshu.com/p/6dd03375224d
 * <p>
 * 客户端往往同时也是一个资源服务器，各个服务之间的通信（访问需要权限的资源）时需携带访问令牌
 * <p>
 * 资源服务器通过 @EnableResourceServer 注解来开启一个 OAuth2AuthenticationProcessingFilter 类型的过滤器
 * 这个过滤器会拦截参数中的 access_token 及 Header 头中是否添加有 Authorization，
 * 并且 Authorization 是以 Bearer 开头的 access_token 才能够识别；
 * 过滤器中相关的接口有 TokenExtractor，其实现类是 BearerTokenExtractor。
 * <p>
 * 通过继承 ResourceServerConfigurerAdapter 类来配置资源服务器
 * <p>
 * OAuth2 为资源服务器配置提供了 ResourceServerProperties 类，
 * 该类会读取配置文件中对资源服务器得配置信息（如授权服务器公钥访问地址）
 *
 * @author alsritter
 * @version 1.0
 **/
@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class OAuth2ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Value("${security.oauth2.authorization.check-token-access}")
    private String checkTokenEndpointUrl;

    /**
     * 这个 ResourceServerProperties 是 OAuth2 为资源服务器配置提供的
     * 该类会读取配置文件中对资源服务器得配置信息（如授权服务器公钥访问地址）
     */
    private final ResourceServerProperties resourceServerProperties;
    private final RedisConnectionFactory redisConnectionFactory;

    @Bean
    public TokenStore tokenStore() {
        return new RedisTokenStore(redisConnectionFactory);
    }

    /**
     * 配置 SpringSecurity 相关信息
     * 允许匿名访问所有接口 主要是 oauth 接口
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()  // 先关闭 CSRF 防护（跨站请求伪造，其实就是使用 Cookie 的那堆屁事，如果使用 JWT 可以直接关闭它）
                .authorizeRequests() // 允许使用 RequestMatcher 实现（即通过URL模式）基于 HttpServletRequest 限制访问
                .antMatchers("/swagger-ui/**", "/swagger-resources/**", "/**/api-docs").permitAll() // 放行swagger
                .antMatchers(HttpMethod.OPTIONS).permitAll()   // 这是跨域请求时浏览器会发的预请求，这里直接放行
                .anyRequest().authenticated();

        // http.authorizeRequests().antMatchers("/test").hasAuthority("DELETE");
    }

    /**
     * 因为使用的是 redis 作为 token 的存储，所以需要特殊配置一下叫做 tokenService 的 Bean，
     * 通过这个 Bean 才能实现 token 的验证。
     */
    @Bean
    public RemoteTokenServices tokenService() {
        RemoteTokenServices tokenService = new RemoteTokenServices();
        tokenService.setClientId(resourceServerProperties.getClientId());
        tokenService.setClientSecret(resourceServerProperties.getClientSecret());
        tokenService.setCheckTokenEndpointUrl(checkTokenEndpointUrl);
        return tokenService;
    }


    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.tokenServices(tokenService());
    }
}
