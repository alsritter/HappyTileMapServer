package com.alsritter.oauth2.config;

import com.alsritter.oauth2.component.JwtTokenEnhancer;
import com.alsritter.oauth2.prop.RsaKeyProperties;
import com.alsritter.oauth2.service.UserService;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.security.KeyPair;
import java.util.ArrayList;

/**
 * 配置 Spring OAuth
 * 说白了这个 OAuth 就是一个 “标准“ 的 发放、验证 Token 的工具，
 * 与之前自己写的 Token 的颁发验证方式没啥区别
 * <p>
 * 具体它们关系可以参考：《 【权限----对比】深入理解Spring Cloud Security、OAuth2、JWT 》
 * https://blog.csdn.net/ningjiebing/article/details/106143265
 * <p>
 * 微服务为什么要使用 OAUth 参考这篇 《 Spring Cloud OAuth2 实现用户认证和单点登录 》
 * https://zhuanlan.zhihu.com/p/88421654
 * <p>
 * 总之就是要用到 Gateway 统一认证就得使用 OAuth
 * <p>
 * 补充知识：OAuth 与 JWT 的关系
 * <p>
 * jwt 只是一种 token 的形式，OAuth 完全可以不用 jwt，哪怕一段随机的字符串也可以当作 token（不考虑安全性、前后端访问压力等因素）
 * oauth 是给第三方应用授权的一套协议，或者说是一种第三方认证的解决方案。本质上，oauth 是通过一系列方法，成功识别出用户的身份和第三方应用身份，
 * 从而根据用户的授权，允许第三方应用从你这边获取用户相关的信息的一种解决方案。
 *
 * @author alsritter
 * @version 1.0
 **/
@Configuration
@EnableAuthorizationServer
@AllArgsConstructor
public class Oauth2ServerConfig extends AuthorizationServerConfigurerAdapter {

    private final JwtTokenEnhancer jwtTokenEnhancer;
    private final PasswordEncoder passwordEncoder;
    private final RsaKeyProperties rsaKeyProperties;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;


    /**
     * JwtAccessTokenConverter 是在 JWT 编码的令牌值和 OAuth 身份验证信息（双向）之间进行转换的转换器。
     */
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setKeyPair(keyPair());
        return jwtAccessTokenConverter;
    }


    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter()); // For JWT. Use in-memory, jdbc, or other if not JWT
    }

    /**
     * 这里是公钥私钥键值对
     */
    @Bean
    public KeyPair keyPair() {
        return rsaKeyProperties.getKeyPair();
    }

    @Bean
    public TokenStore jwtTokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }


    /**
     * 使用这个 @Primary 注解可用保证它是最优先使用的 DefaultTokenServices 的 Bean，说白了就是 @Override 的作用
     * 原本这个 DefaultTokenServices 是在下面 AuthorizationServerEndpointsConfigurer 里面装配的
     * 可用点进 AuthorizationServerEndpointsConfigurer 源码去搜索 createDefaultTokenServices 方法就懂了
     *
     * 这里使用默认的就行了
     */
    @Bean
    @Primary
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(jwtTokenStore());
        defaultTokenServices.setSupportRefreshToken(true);  // 启用刷新令牌
        return defaultTokenServices;
    }

    /**
     * 用来配置令牌端点的安全约束
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        // 这里的 checkTokenAccess 和 tokenKeyAccess 配置对应于资源服务器的配置文件 security.oauth2.client 下面的配置项
        security.checkTokenAccess("isAuthenticated()")           // 只允许验证用户访问令牌解析端点（/oauth/check_token）
                .tokenKeyAccess("permitAll()")                   // 允许所有资源服务器访问公钥端点（/oauth/token_key）
                .allowFormAuthenticationForClients()             // 允许客户端发送表单来进行权限认证来获取令牌（默认是 URL 的 Params）
                .passwordEncoder(passwordEncoder);
    }

    /**
     * 配置授权服务器端点的属性和增强功能。
     * 这里使用 JWT 作为 Token
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        ArrayList<TokenEnhancer> delegates = new ArrayList<>();
        delegates.add(jwtTokenEnhancer);
        delegates.add(accessTokenConverter());
        val enhancerChain = new TokenEnhancerChain();
        enhancerChain.setTokenEnhancers(delegates); // 配置 JWT 的内容增强器


        endpoints
                .userDetailsService(userService)              // 设置用户验证服务。
                .authenticationManager(authenticationManager) // 密码授予的 AuthenticationManager，调用此方法才能支持 password 模式。
                .accessTokenConverter(accessTokenConverter())
                .tokenEnhancer(enhancerChain)
                .tokenStore(jwtTokenStore());                 // 设置 Token 存储方式
    }


    /**
     * 用来配置客户端详情服务，客户端详情信息在这里进行初始化，这里直接把把客户端详情信息写死
     * 一般就 web 端 客户端，用到再添加就好了，没有那么多奇奇怪怪的设备
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("webClient")                         // 配置 client_id
                .secret(passwordEncoder.encode("secret"))    // 配置 client_secret
                .scopes("all")                                          // 配置申请的权限范围，此处的 scopes 是无用的，可以随意设置
                .authorizedGrantTypes("password", "refresh_token")      // grant_type 表示授权类型, 密码模式 password
                .accessTokenValiditySeconds(3600)                       // Access Token (访问 Token)的有效期
                .refreshTokenValiditySeconds(86400);                    // Refresh Token (刷新 token) 的有效期 (24 小时)
    }
}
