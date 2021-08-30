package com.alsritter.oauth2_2.config;

import com.alsritter.oauth2_2.component.MyRemoteCallProperties;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;

/**
 * @author alsritter
 * @version 1.0
 **/
@Configuration
@AllArgsConstructor
public class ClientDetailsServiceConfig {

    private final PasswordEncoder passwordEncoder;
    private final MyRemoteCallProperties.ClientWeb remoteCallProperties;

    @Bean
    public ClientDetailsService clientDetailsService(ClientDetailsServiceConfigurer clients) throws Exception {
        Integer accessTokenValiditySeconds = remoteCallProperties.getAccessTokenValiditySeconds();
        Integer refreshTokenValiditySeconds = remoteCallProperties.getRefreshTokenValiditySeconds();

        return clients.inMemory()
                .withClient("webClient")                         // 配置 client_id
                .secret(passwordEncoder.encode("secret"))    // 配置 client_secret
                .scopes("web")                                          // 配置申请的权限范围，此处的 scopes 是无用的，可以随意设置
                .authorizedGrantTypes("password", "refresh_token")      // grant_type 表示授权类型, 密码模式 password
                // Spring Security OAuth2 not using token expire values from properties
                // https://stackoverflow.com/questions/47292961/spring-security-oauth2-not-using-token-expire-values-from-properties/51784859
                .accessTokenValiditySeconds(accessTokenValiditySeconds)
                .refreshTokenValiditySeconds(refreshTokenValiditySeconds)

                .and()
                .withClient("interiorClient")
                .secret(passwordEncoder.encode("secret"))
                // 客户端凭证验证在 ClientCredentialsTokenEndpointFilter 里面
                .authorizedGrantTypes("client_credentials", "refresh_token")             // 使用凭证式认证
                // ClientDetailsUserDetailsService 就是默认的客户端凭证 UserDetailsService
                .scopes("all")
                .and().build();
    }
}
