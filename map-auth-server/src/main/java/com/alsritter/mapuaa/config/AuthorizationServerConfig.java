package com.alsritter.mapuaa.config;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.sql.DataSource;
import java.util.Collections;

/**
 * @author alsritter
 * @version 1.0
 **/
@Configuration
@EnableAuthorizationServer
@Setter(onMethod_ = {@Autowired})
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
    private AuthenticationManager authenticationManager;                //从WebSecurityConfig中获取的
    private AuthorizationCodeServices authorizationCodeServices;        //本类中的，授权码模式需要
    private TokenStore tokenStore;                                      //TokenConfig中的
    private PasswordEncoder passwordEncoder;                            //从WebSecurityConfig中获取的
    private ClientDetailsService clientDetailsService;                  //本类中的
    private JwtAccessTokenConverter jwtAccessTokenConverter;            //TokenConfig中的

    /**
     * 从数据库中获取客户端详情，这里调用的 JdbcClientDetailsService 工具类可以自动帮忙查询，
     * ClientDetailsService 的信息是从 oauth_client_details 这张表里查出来的，
     * 点击工具类源码可以看到它里面执行的 SQL 就是查询 oauth_client_details 这表，
     * 所以我们的数据库中只要创建出这张表，表里再添加这些字段即可。
     *
     * @param dataSource
     * @return
     */
    @Bean
    public ClientDetailsService clientDetailsService(DataSource dataSource) {
        ClientDetailsService clientDetailsService = new JdbcClientDetailsService(dataSource);
        ((JdbcClientDetailsService) clientDetailsService).setPasswordEncoder(passwordEncoder);
        return clientDetailsService;
    }

    /**
     * 设置授权码模式的授权码从数据库中获取，这个 JdbcAuthorizationCodeServices 工具类很相似，
     * 这个工具类是从 oauth_code 这个表查询的
     *
     * @param dataSource
     * @return 返回一个授权码
     */
    @Bean
    public AuthorizationCodeServices authorizationCodeServices(DataSource dataSource) {
        return new JdbcAuthorizationCodeServices(dataSource);
    }

    /**
     * 令牌管理服务
     * <p>
     * 为了方便管理，我们使用 TokenConfig 这个类去配置 Token 相关的内容。
     * 添加了 @Bean 注解将其添加到 Spring 容器后就可以在其它的类中去注入使用了。
     * <p>
     * 这里采用了 JWT 令牌管理方式，然后使用了对称密钥去进行加密。还有另外几种令牌管理方式：
     * InMemoryTokenStore：在内存中存储令牌（默认）
     * JdbcTokenStore：令牌存储在数据库中
     * RedisTokenStore：令牌存储在 Redis 中
     *
     * @return 返回一个令牌
     */
    @Bean
    public AuthorizationServerTokenServices tokenService() {
        // DefaultTokenServices 使用随机 UUID 值作为访问令牌和刷新令牌值的令牌服务的基本实现。
        // 它的自定义的主要扩展点是 TokenEnhancer ，它将在生成访问和刷新令牌之后但在存储之前调用。
        // 存储方式 被委托给 TokenStore 实现，并将访问令牌的定制委托给 TokenEnhancer。
        DefaultTokenServices service = new DefaultTokenServices();
        service.setClientDetailsService(clientDetailsService);  //客户端信息服务
        service.setSupportRefreshToken(true);                   //支持自动刷新

        // 设置它的存储方式（这个 tokenStore 是上面传入的 TokenConfig 配置的 JWT）
        service.setTokenStore(tokenStore);

        // 令牌增强，这里可以设置令牌的加密方式之类的操作
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        // 设置为在 TokenConfig 配置的对称加密
        tokenEnhancerChain.setTokenEnhancers(Collections.singletonList(jwtAccessTokenConverter));
        service.setTokenEnhancer(tokenEnhancerChain);

        service.setAccessTokenValiditySeconds(7200);        //令牌默认有效期2小时
        service.setRefreshTokenValiditySeconds(259200);     //刷新令牌默认有效期3天
        return service;
    }

    /**
     * 用来配置令牌端点的安全约束
     *
     * @param security
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("permitAll")            // /oauth/token_key     提供公有密匙的端点 允许任何人访问
                .checkTokenAccess("permitAll")          // /oauth/check_token    用于资源服务访问的令牌解析端点 允许任何人访问
                .allowFormAuthenticationForClients();   //表单认证（申请令牌）
    }

    /**
     * 用来配置客户端详情服务,客户端详情信息在这里进行初始化,
     * 你能够把客户端详情信息写死在这里或者是通过数据库来存储调取详情信息
     *
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetailsService);
    }

    /**
     * 用来配置令牌（token）的访问端点（url）和令牌服务(token services)
     *
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager)          //认证管理器,密码模式需要
                .authorizationCodeServices(authorizationCodeServices)   //授权码服务,授权码模式需要
                .tokenServices(tokenService())
                .allowedTokenEndpointRequestMethods(HttpMethod.POST);   //允许post提交
    }

}
