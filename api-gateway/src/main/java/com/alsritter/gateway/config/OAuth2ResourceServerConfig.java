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
        log.debug("token ??????????????? {}", checkTokenEndpointUrl);

        http.oauth2ResourceServer()
                .opaqueToken()
                .introspectionClientCredentials(tokenConfig.getClientId(), tokenConfig.getClientSecret())
                .introspectionUri(tokenConfig.getCheckTokenEndpointUrl())
                // ??????????????????????????? Token ?????????
                .introspector(new CustomNimbusReactiveOpaqueTokenIntrospector(
                        checkTokenEndpointUrl, webClient));

        //??????????????????????????????????????????????????????
        http.oauth2ResourceServer()
                .authenticationEntryPoint(restAuthenticationEntryPoint);

        // ???????????????????????????????????????????????????
        // ?????? https://stackoverflow.com/questions/52740163/web-ignoring-using-spring-webflux
        // ?????????????????????????????????????????????????????? .pathMatchers(ArrayUtil.toArray(ignoreUrlsConfig.getUrls(), String.class)).permitAll()
        // ??????????????? Authorization ????????????????????????
        // ??????????????????????????????http.permitAll()???web.ignoring()??????????
        // https://www.cnblogs.com/panchanggui/p/14975963.html
        // http.securityMatcher(new NegatedServerWebExchangeMatcher(
        //         ServerWebExchangeMatchers.pathMatchers(ArrayUtil.toArray(ignoreUrlsConfig.getUrls(), String.class))));

        http.securityMatcher(new NegatedServerWebExchangeMatcher(customServerWebExchangeMatcher));

        //?????????????????????????????????JWT?????????
        http.addFilterBefore(ignoreUrlsRemoveJwtFilter, SecurityWebFiltersOrder.AUTHENTICATION);
        http.authorizeExchange()
                // .pathMatchers(ArrayUtil.toArray(ignoreUrlsConfig.getUrls(), String.class)).permitAll()
                .anyExchange().access(authorizationManager)//?????????????????????
                .and()
                .exceptionHandling()
                .accessDeniedHandler(restfulAccessDeniedHandler)       //???????????????
                .authenticationEntryPoint(restAuthenticationEntryPoint)//???????????????
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
        config.setAllowCredentials(true); // ?????? cookies ??????
//        config.setAllowedOrigins(Arrays.asList(origin));
        config.setMaxAge(18000L);// ???????????????????????????????????????????????????????????????????????????????????????????????????????????????
        config.addAllowedMethod("OPTIONS");// ??????????????????????????????* ??????????????????
        config.addAllowedMethod("GET");// ?????? Get ???????????????
        config.addAllowedMethod("POST");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(new PathPatternParser());
        source.registerCorsConfiguration("/**", config);
        return new CorsWebFilter(source);
    }
}
