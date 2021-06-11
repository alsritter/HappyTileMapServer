package com.alsritter.oauth2.config;

import com.alsritter.oauth2.component.JwtTokenEnhancer;
import com.alsritter.oauth2.prop.RsaKeyProperties;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.security.KeyPair;
import java.util.ArrayList;

/**
 * @author alsritter
 * @version 1.0
 **/
@Configuration
@AllArgsConstructor
public class JwtTokenStoreConfig {

    private final RsaKeyProperties rsaKeyProperties;
    private final JwtTokenEnhancer jwtTokenEnhancer;

    /**
     * 这里是公钥私钥键值对
     */
    @Bean
    public KeyPair keyPair() {
        return rsaKeyProperties.getKeyPair();
    }

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
    public TokenStore jwtTokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    /**
     * 不设置这个会报错 Handling error: UnsupportedGrantTypeException, Unsupported grant type: password
     *
     * 使用这个 @Primary 注解可用保证它是最优先使用的 DefaultTokenServices 的 Bean，说白了就是 @Override 的作用
     * 原本这个 DefaultTokenServices 是在下面 AuthorizationServerEndpointsConfigurer 里面装配的
     * 可用点进 AuthorizationServerEndpointsConfigurer 源码去搜索 createDefaultTokenServices 方法就懂了
     * <p>
     * 这里使用默认的就行了
     *
     * 在自定义的 CustomAuthenticationSuccessHandler 中可以调用这个 Bean 生成 Token
     */
    @Bean("jwtTokenServices")
    @Primary
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(jwtTokenStore());
        defaultTokenServices.setSupportRefreshToken(true);  // 启用刷新令牌

        ArrayList<TokenEnhancer> delegates = new ArrayList<>();
        delegates.add(jwtTokenEnhancer);
        delegates.add(accessTokenConverter());

        val enhancerChain = new TokenEnhancerChain();
        enhancerChain.setTokenEnhancers(delegates);                 // 配置 JWT 的内容增强器
        defaultTokenServices.setTokenEnhancer(enhancerChain);       // 这个很重要


        // 访问令牌的默认有效性（以秒为单位）。 非过期令牌为零或负数。 如果设置了客户端详细信息服务，则将从客户端读取有效期，如果客户端未定义，则默认为该值。
        defaultTokenServices.setAccessTokenValiditySeconds(43200);  // 令牌有效期12小时
        defaultTokenServices.setRefreshTokenValiditySeconds(259200);// 刷新令牌有效期3天



        return defaultTokenServices;
    }

}
