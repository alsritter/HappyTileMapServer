package com.alsritter.gateway.filter;

import com.alsritter.gateway.config.SecureProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * 白名单路径访问时需要移除 Token 请求头
 *
 * @author alsritter
 * @version 1.0
 **/
@Component
@RequiredArgsConstructor
public class IgnoreUrlsRemoveJwtFilter implements WebFilter {
    private final SecureProperties.IgnoreUrl ignoreUrlsConfig;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        // ServerHttpRequest request = exchange.getRequest();
        // URI uri = request.getURI();
        // PathMatcher pathMatcher = new AntPathMatcher();
        // // 白名单路径移除 Token 请求头
        // List<String> ignoreUrls = ignoreUrlsConfig.getUrls();
        // for (String ignoreUrl : ignoreUrls) {
        //     if (pathMatcher.match(ignoreUrl, uri.getPath())) {
        //         exchange = exchange.mutate().request(request).build();
        //         return chain.filter(exchange);
        //     }
        // }

        return chain.filter(exchange);
    }
}
