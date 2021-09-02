package com.alsritter.gateway.config;

import cn.hutool.core.util.ArrayUtil;
import com.alsritter.common.AppConstant;
import com.alsritter.gateway.component.CustomNimbusReactiveOpaqueTokenIntrospector;
import com.alsritter.gateway.component.CustomServerWebExchangeMatcher;
import com.alsritter.gateway.component.RestAuthenticationEntryPoint;
import com.alsritter.gateway.component.RestfulAccessDeniedHandler;
import com.alsritter.gateway.filter.IgnoreUrlsRemoveJwtFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.NegatedServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.pattern.PathPatternParser;

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
    private final RestfulAccessDeniedHandler restfulAccessDeniedHandler;
    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    private final IgnoreUrlsRemoveJwtFilter ignoreUrlsRemoveJwtFilter;
    private final SecureProperties.TokenConfig tokenConfig;
    private final WebClient.Builder webClient;
    private final CustomServerWebExchangeMatcher customServerWebExchangeMatcher;


    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http.addFilterAt(corsFilter(), SecurityWebFiltersOrder.CORS);
        String url = "http://" + AppConstant.APPLICATION_OAUTH2_NAME;
        String checkTokenEndpointUrl = url + tokenConfig.getCheckTokenEndpointUrl();
        log.debug("token 检查地址为 {}", checkTokenEndpointUrl);

        http.oauth2ResourceServer()
                .opaqueToken()
                .introspectionClientCredentials(tokenConfig.getClientId(), tokenConfig.getClientSecret())
                .introspectionUri(tokenConfig.getCheckTokenEndpointUrl())
                // 要自己写一个不透明 Token 转换器
                .introspector(new CustomNimbusReactiveOpaqueTokenIntrospector(
                        checkTokenEndpointUrl, webClient));

        //自定义处理请求头过期或签名错误的结果
        http.oauth2ResourceServer()
                .authenticationEntryPoint(restAuthenticationEntryPoint);

        // 这里是忽略哪些链接，使之不走过滤链
        // 参考 https://stackoverflow.com/questions/52740163/web-ignoring-using-spring-webflux
        // 总之白名单应该使用这个，否则下面这种 .pathMatchers(ArrayUtil.toArray(ignoreUrlsConfig.getUrls(), String.class)).permitAll()
        // 如果加上了 Authorization 还是会走认证过滤
        // 具体两个的区别参考：http.permitAll()与web.ignoring()的区别?
        // https://www.cnblogs.com/panchanggui/p/14975963.html
        // http.securityMatcher(new NegatedServerWebExchangeMatcher(
        //         ServerWebExchangeMatchers.pathMatchers(ArrayUtil.toArray(ignoreUrlsConfig.getUrls(), String.class))));

        http.securityMatcher(new NegatedServerWebExchangeMatcher(customServerWebExchangeMatcher));

        //对白名单路径，直接移除JWT请求头
        http.addFilterBefore(ignoreUrlsRemoveJwtFilter, SecurityWebFiltersOrder.AUTHENTICATION);
        http.authorizeExchange()
                // .pathMatchers(ArrayUtil.toArray(ignoreUrlsConfig.getUrls(), String.class)).permitAll()
                .anyExchange().access(authorizationManager)//鉴权管理器配置
                .and()
                .exceptionHandling()
                .accessDeniedHandler(restfulAccessDeniedHandler)       //处理未授权
                .authenticationEntryPoint(restAuthenticationEntryPoint)//处理未认证
                .and().csrf().disable();
        return http.build();
    }

    // @Value("${server.port}")
    // private int port;

    // public String getUrl() {
    //     String address = null;
    //     try {
    //         InetAddress host = InetAddress.getLocalHost();
    //         address = host.getHostAddress();
    //     } catch (UnknownHostException e) {
    //         address = "127.0.0.1";
    //     }
    //     return "http://" + address + ":" + this.port;
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
