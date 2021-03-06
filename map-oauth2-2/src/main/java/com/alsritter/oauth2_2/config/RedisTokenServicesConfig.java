package com.alsritter.oauth2_2.config;

import com.alsritter.oauth2_2.component.MyRemoteCallProperties;
import com.alsritter.oauth2_2.component.RedisTokenEnhancer;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import java.util.ArrayList;

/**
 * @author alsritter
 * @version 1.0
 **/
@Configuration
@AllArgsConstructor
public class RedisTokenServicesConfig {

    /**
     * redis 工厂，默认使用 lettue
     */
    private final RedisConnectionFactory redisConnectionFactory;
    private final RedisTokenEnhancer redisTokenEnhancer;
    private final ClientDetailsService clientDetailsService;


    @Bean
    public TokenStore redisTokenStore() {
        //使用redis存储token
        return new RedisTokenStore(redisConnectionFactory);
    }

    /**
     * 不设置这个会报错 Handling error: UnsupportedGrantTypeException, Unsupported grant type: password
     * <p>
     * 使用这个 @Primary 注解可用保证它是最优先使用的 DefaultTokenServices 的 Bean，说白了就是 @Override 的作用
     * 原本这个 DefaultTokenServices 是在下面 AuthorizationServerEndpointsConfigurer 里面装配的
     * 可用点进 AuthorizationServerEndpointsConfigurer 源码去搜索 createDefaultTokenServices 方法就懂了
     * <p>
     * 这里使用默认的就行了
     * <p>
     * 在自定义的 CustomAuthenticationSuccessHandler 中可以调用这个 Bean 生成 Token
     */
    @Bean("redisTokenServices")
    @Primary
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(redisTokenStore());
        defaultTokenServices.setSupportRefreshToken(true);  // 启用刷新令牌
        ArrayList<TokenEnhancer> delegates = new ArrayList<>();
        delegates.add(redisTokenEnhancer);

        val enhancerChain = new TokenEnhancerChain();
        enhancerChain.setTokenEnhancers(delegates);                 // 配置内容增强器
        defaultTokenServices.setTokenEnhancer(enhancerChain);       // 这个很重要
        defaultTokenServices.setClientDetailsService(clientDetailsService);
        // 访问令牌的默认有效性（以秒为单位）。 非过期令牌为零或负数。 如果设置了客户端详细信息服务，则将从客户端读取有效期，如果客户端未定义，则默认为该值。
        // 默认走 DefaultTokenServices.getAccessTokenValiditySeconds 取客户凭证的过期时间，所以这里设置是无效的
        // defaultTokenServices.setAccessTokenValiditySeconds(remoteCallProperties.getAccessTokenValiditySeconds());
        // defaultTokenServices.setRefreshTokenValiditySeconds(remoteCallProperties.getRefreshTokenValiditySeconds());

        // 注意！！！ redisTokenStore 的策略是这个 Token 的过期时间，即为这个 Key 的存活时间，
        // 所以是无法通过 Expiration 来取得过期时间的（因为过期直接就没有了~~）
        return defaultTokenServices;
    }
}
