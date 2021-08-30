package com.alsritter.oauth2_2.config;

import com.alsritter.oauth2_2.component.MyRemoteCallProperties;
import com.alsritter.oauth2_2.service.UserService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

@Configuration
@EnableAuthorizationServer
@Setter(onMethod_ = {@Autowired})
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    private PasswordEncoder passwordEncoder;
    private UserService userService;
    private ClientDetailsService clientDetailsService;


    @Qualifier("redisTokenServices")
    private AuthorizationServerTokenServices redisTokenServices;

    /**
     * 用来配置令牌端点的安全约束
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        // 这里的 checkTokenAccess 和 tokenKeyAccess 配置对应于资源服务器的配置文件 security.oauth2.client 下面的配置项
        security
                // .checkTokenAccess("isAuthenticated()")           // 只允许验证用户访问令牌解析端点（/oauth/check_token）
                .checkTokenAccess("permitAll()")
                .tokenKeyAccess("permitAll()")                   // 允许所有资源服务器访问公钥端点（/oauth/token_key）
                .allowFormAuthenticationForClients()             // 允许客户端发送表单来进行权限认证来获取令牌（默认是 URL 的 Params）
                .passwordEncoder(passwordEncoder);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // 用来配置令牌（token）的访问端点
        endpoints
                .tokenServices(redisTokenServices)
                .userDetailsService(userService);

        // 就是下面配置的 inMemory 对应的工具类为 InMemoryClientDetailsService
        // endpoints.setClientDetailsService()
    }


    /**
     * 用来配置客户端详情服务，客户端详情信息在这里进行初始化，这里直接把把客户端详情信息写死
     * 一般就 web 端 客户端，用到再添加就好了，没有那么多奇奇怪怪的设备
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // Integer accessTokenValiditySeconds = remoteCallProperties.getAccessTokenValiditySeconds();
        // Integer refreshTokenValiditySeconds = remoteCallProperties.getRefreshTokenValiditySeconds();
        //
        // clients.inMemory()
        //         .withClient("webClient")                         // 配置 client_id
        //         .secret(passwordEncoder.encode("secret"))    // 配置 client_secret
        //         .scopes("web")                                          // 配置申请的权限范围，此处的 scopes 是无用的，可以随意设置
        //         .authorizedGrantTypes("password", "refresh_token")      // grant_type 表示授权类型, 密码模式 password
        //         // Spring Security OAuth2 not using token expire values from properties
        //         // https://stackoverflow.com/questions/47292961/spring-security-oauth2-not-using-token-expire-values-from-properties/51784859
        //         .accessTokenValiditySeconds(accessTokenValiditySeconds)
        //         .refreshTokenValiditySeconds(refreshTokenValiditySeconds)
        //
        //         .and()
        //         .withClient("interiorClient")
        //         .secret(passwordEncoder.encode("secret"))
        //         // 客户端凭证验证在 ClientCredentialsTokenEndpointFilter 里面
        //         .authorizedGrantTypes("client_credentials", "refresh_token")             // 使用凭证式认证
        //         // ClientDetailsUserDetailsService 就是默认的客户端凭证 UserDetailsService
        //         .scopes("all");
        clients.withClientDetails(clientDetailsService);
    }
}
