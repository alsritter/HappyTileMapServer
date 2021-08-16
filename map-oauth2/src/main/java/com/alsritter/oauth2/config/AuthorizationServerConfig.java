package com.alsritter.oauth2.config;

import com.alsritter.oauth2.service.UserService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

/**
 * 配置 Spring OAuth
 * 说白了这个 OAuth 就是一个 “标准“ 的 发放、验证 Token 的工具，
 * 与之前自己写的 Token 的颁发验证方式没啥区别
 * <p>
 * 注意！！ Spring OAuth 它与 Spring Security 其实没有什么关系，可以把它们当成互相平行的关系（无关联），Spring OAuth 它并不会与
 * Spring Security 有什么牵扯，它本质就是预留了几个标准的端点，等 Spring Security 登陆完成后，既可以通过调用 /oauth/token API 返回 Token
 * 也可以在 Spring Security 的 AuthenticationSuccessHandler 里面自己调用 AuthorizationServerTokenServices 端点生成 Token
 * 返回给前端，所以不要把它们的关系想太复杂了
 * <p>
 * <p>
 * ===============拓展资料===============
 * <p>
 * jwt 只是一种 token 的形式，OAuth 完全可以不用 jwt，哪怕一段随机的字符串也可以当作 token（不考虑安全性、前后端访问压力等因素）
 * oauth 是给第三方应用授权的一套协议，或者说是一种第三方认证的解决方案。本质上，oauth 是通过一系列方法，成功识别出用户的身份和第三方应用身份，
 * 从而根据用户的授权，允许第三方应用从你这边获取用户相关的信息的一种解决方案。
 * <p>
 * <p>
 * 具体它们关系可以参考：《【权限----对比】深入理解Spring Cloud Security、OAuth2、JWT》
 * https://blog.csdn.net/ningjiebing/article/details/106143265
 * <p>
 * 微服务为什么要使用 OAUth 参考这篇 《 Spring Cloud OAuth2 实现用户认证和单点登录 》
 * https://zhuanlan.zhihu.com/p/88421654
 * <p>
 * 总之就是要用到 Gateway 统一认证就得使用 OAuth
 * <p>
 * 补充知识：OAuth 与 JWT 的关系
 * <p>
 *
 * @author alsritter
 * @version 1.0
 **/
@Configuration
@EnableAuthorizationServer
@Setter(onMethod_ = {@Autowired})
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    private PasswordEncoder passwordEncoder;
    private UserService userService;
    private AuthenticationManager authenticationManager;
    private JwtAccessTokenConverter accessTokenConverter;
    private TokenStore jwtTokenStore;

    @Qualifier("jwtTokenServices")
    private AuthorizationServerTokenServices jwtTokenServices;


    /**
     * 用来配置令牌端点的安全约束
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        // 这里的 checkTokenAccess 和 tokenKeyAccess 配置对应于资源服务器的配置文件 security.oauth2.client 下面的配置项
        security
                .checkTokenAccess("isAuthenticated()")           // 只允许验证用户访问令牌解析端点（/oauth/check_token）
                .tokenKeyAccess("permitAll()")                   // 允许所有资源服务器访问公钥端点（/oauth/token_key）
                .allowFormAuthenticationForClients()             // 允许客户端发送表单来进行权限认证来获取令牌（默认是 URL 的 Params）
                .passwordEncoder(passwordEncoder);
        // security.accessDeniedHandler()
    }

    /**
     * 注意，无法使用自定义的 JWT 存储器参考这个回答
     * <p>
     * Custom DefaultTokenServices bean not being used
     * https://stackoverflow.com/questions/49077401/custom-defaulttokenservices-bean-not-being-used
     * <p>
     * 配置授权服务器端点的属性和增强功能。
     * 这里使用 JWT 作为 Token
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

        // 用来配置令牌（token）的访问端点
        endpoints
                // 别忘了设置 tokenServices，不设置这个默认走的是这个 AuthorizationServerEndpointsConfigurer 内部定义的 TokenService
                .tokenServices(jwtTokenServices)
                // 说明：上面这个 tokenServices 设置项与下面 authenticationManager、accessTokenConverter、tokenStore 设置项的关系
                // 下面的这些配置项是用来设置这个 AuthorizationServerEndpointsConfigurer 端点内置的 tokenServices
                // 所以，如果使用了 tokenServices 这个设置项，则这些配置是无效的

                .userDetailsService(userService);                    // 设置用户验证服务。
                // .authenticationManager(authenticationManager)       // 密码授予的 AuthenticationManager，调用此方法才能支持 password 模式。
                // .accessTokenConverter(accessTokenConverter)
                // .tokenStore(jwtTokenStore);                         // 设置 Token 存储方式
        // .allowedTokenEndpointRequestMethods(HttpMethod.POST);// 允许post提交
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
                .refreshTokenValiditySeconds(86400)                    // Refresh Token (刷新 token) 的有效期 (24 小时)

                .and()//下面配置第二个应用
                .withClient("test")
                .scopes("testSc")
                .accessTokenValiditySeconds(7200)
                .scopes("all");

        // .and()
        // // client_id，用户账号
        // .withClient("c1")
        // // 客户端密钥
        // .secret(passwordEncoder.encode("secret"))
        // // 资源列表，资源标识
        // .resourceIds("user")
        // // 授权类型（4种）
        // .authorizedGrantTypes("authorization_code", "password", "client_credentials", "implicit","refresh_token")
        // // 客戶端允许的授权范围
        // .scopes("all")
        // // false跳转到授权页面，让用户点击授权，如果是true,相当于自动点击授权，就不跳转授权页面
        // .autoApprove(true)
        // // 加上验证回调地址，返回授权码信息
        // // 注意！！ 重定向地址只有 authorization_code 或 implicit 能使用
        // .redirectUris("http://localhost:9527/auth/token.html");
    }

}
