package com.alsritter.service.test.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

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
@AllArgsConstructor
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class OAuth2ResourceServerConfig extends ResourceServerConfigurerAdapter {

    /**
     * 这个 ResourceServerProperties 是 OAuth2 为资源服务器配置提供的
     * 该类会读取配置文件中对资源服务器得配置信息（如授权服务器公钥访问地址）
     */
    private final ResourceServerProperties resourceServerProperties;

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        //设置用于解码的非对称加密的公钥
        converter.setVerifierKey(getPubKey());
        return converter;
    }

    /**
     * 这里展示两种获取公钥的方式
     * <p>
     * 本地公钥
     *
     * @return 公钥
     */
    private String getPubKey() {
        Resource resource = new ClassPathResource("public.txt");
        String publicKey;
        try {
            publicKey = new String(FileCopyUtils.copyToByteArray(resource.getInputStream()));
        } catch (IOException e) {
            // 找不到本地公钥才找远程的公钥
            return getKeyFromAuthorizationServer();
        }

        return publicKey;
    }


    /**
     * 联网公钥 参考自：
     * https://www.jianshu.com/p/6dd03375224d
     *
     * @return 公钥
     */
    private String getKeyFromAuthorizationServer() {
        System.out.println(resourceServerProperties.getJwt().getKeyUri());
        ObjectMapper objectMapper = new ObjectMapper();
        String pubKey = new RestTemplate().getForObject(resourceServerProperties.getJwt().getKeyUri(), String.class);

        try {
            log.info("联网公钥");
            return objectMapper.readValue(pubKey, Map.class).get("value").toString();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 配置 SpringSecurity 相关信息
     * 允许匿名访问所有接口 主要是 oauth 接口
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()  // 先关闭 CSRF 防护（跨站请求伪造，其实就是使用 Cookie 的那堆屁事，如果使用 JWT 可以直接关闭它）
                .authorizeRequests() // 允许使用 RequestMatcher 实现（即通过URL模式）基于 HttpServletRequest 限制访问
                .antMatchers(HttpMethod.OPTIONS).permitAll();   // 这是跨域请求时浏览器会发的预请求，这里直接放行

        // http.authorizeRequests().antMatchers("/test").hasAuthority("DELETE");

        http.authorizeRequests().anyRequest().authenticated();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);    //禁用session
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources
                // 资源id，与认证服务器对应，如果资源id不一致会认证失败
                // .resourceId("user")
                //                // 令牌服务
                //                .tokenServices(tokenService())
                // 令牌服务
                .tokenStore(tokenStore())
                .stateless(true);
        // //认证异常
        // .authenticationEntryPoint((httpServletRequest, httpServletResponse, e) -> {
        //     ObjectMapper objectMapper= new ObjectMapper();
        //     httpServletResponse.setContentType("application/json;charset=UTF-8");
        //     httpServletResponse.getWriter().write(objectMapper.writeValueAsString(R.error("认证异常")));
        // })
        //
        // //无权访问
        // .accessDeniedHandler((httpServletRequest, httpServletResponse, e) -> {
        //     ObjectMapper objectMapper=new ObjectMapper();
        //     httpServletResponse.setContentType("application/json;charset=UTF-8");
        //     httpServletResponse.getWriter().write(objectMapper.writeValueAsString(R.error("无权访问")));
        // });
    }

    // /**
    //  * 负责将Jwt转换为身份验证。
    //  */
    // Converter<Jwt, ? extends AbstractAuthenticationToken> grantedAuthoritiesExtractor() {
    //     return new JwtAuthenticationConverter();
    // }
}
