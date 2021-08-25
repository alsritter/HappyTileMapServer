package com.alsritter.gateway.config;

import cn.hutool.core.util.ArrayUtil;
import com.alsritter.gateway.component.MyNimbusReactiveOpaqueTokenIntrospector;
import com.alsritter.gateway.component.RestAuthenticationEntryPoint;
import com.alsritter.gateway.component.RestfulAccessDeniedHandler;
import com.alsritter.gateway.filter.IgnoreUrlsRemoveJwtFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.util.pattern.PathPatternParser;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author alsritter
 * @version 1.0
 **/
@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableWebFluxSecurity
public class OAuth2ResourceServerConfig {

    private final AuthorizationManager authorizationManager;
    private final SecureProperties.IgnoreUrl ignoreUrlsConfig;
    private final RestfulAccessDeniedHandler restfulAccessDeniedHandler;
    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    private final IgnoreUrlsRemoveJwtFilter ignoreUrlsRemoveJwtFilter;
    private final SecureProperties.TokenConfig tokenConfig;

    @Value("${server.port}")
    private int port;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        // http.oauth2ResourceServer().jwt()
        //         .jwtAuthenticationConverter(jwtAuthenticationConverter());
        // 配置不透明的令牌
        // Map<String, Object> attributes = x.getAttributes();
        // Collection<? extends GrantedAuthority> collection = (Collection<? extends GrantedAuthority>) attributes.get("authorities");

        http.addFilterAt(corsFilter(), SecurityWebFiltersOrder.CORS);

        String checkTokenEndpointUrl = getUrl() + tokenConfig.getCheckTokenEndpointUrl();
        log.debug("token 检查地址为 {}", checkTokenEndpointUrl);

        http.oauth2ResourceServer().opaqueToken()
                .introspectionClientCredentials(tokenConfig.getClientId(), tokenConfig.getClientSecret())
                .introspectionUri(tokenConfig.getCheckTokenEndpointUrl())
                // 要自己写一个不透明 Token 转换器
                .introspector(new MyNimbusReactiveOpaqueTokenIntrospector(
                        checkTokenEndpointUrl, tokenConfig.getClientId(), tokenConfig.getClientSecret()));

        //自定义处理请求头过期或签名错误的结果
        http.oauth2ResourceServer().authenticationEntryPoint(restAuthenticationEntryPoint);
        //对白名单路径，直接移除JWT请求头
        http.addFilterBefore(ignoreUrlsRemoveJwtFilter, SecurityWebFiltersOrder.AUTHENTICATION);
        http.authorizeExchange()
                .pathMatchers(ArrayUtil.toArray(ignoreUrlsConfig.getUrls(), String.class)).permitAll()//白名单配置
                .anyExchange().access(authorizationManager)//鉴权管理器配置
                .and().exceptionHandling()
                .accessDeniedHandler(restfulAccessDeniedHandler)       //处理未授权
                .authenticationEntryPoint(restAuthenticationEntryPoint)//处理未认证
                .and().csrf().disable();
        return http.build();
    }

    public String getUrl() {
        String address = null;
        try {
            InetAddress host = InetAddress.getLocalHost();
            address = host.getHostAddress();
        } catch (UnknownHostException e) {
            address = "127.0.0.1";
        }
        return "http://" + address + ":" + this.port;
    }

    // @Bean
    // public Converter<Jwt, ? extends Mono<? extends AbstractAuthenticationToken>> jwtAuthenticationConverter() {
    //     JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
    //     jwtGrantedAuthoritiesConverter.setAuthorityPrefix(AuthConstant.AUTHORITY_PREFIX);
    //     jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName(AuthConstant.AUTHORITY_CLAIM_NAME);
    //     JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
    //     jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
    //     return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
    // }

    @Bean
    public CorsWebFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.setAllowCredentials(true); // 允许 cookies 跨域
//        config.setAllowedOrigins(Arrays.asList(origin));
        config.setMaxAge(18000L);// 预检请求的缓存时间（秒），即在这个时间段里，对于相同的跨域请求不会再预检了
        config.addAllowedMethod("OPTIONS");// 允许提交请求的方法，* 表示全部允许
        config.addAllowedMethod("GET");// 允许 Get 的请求方法
        config.addAllowedMethod("POST");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(new PathPatternParser());
        source.registerCorsConfiguration("/**", config);
        return new CorsWebFilter(source);
    }
}
