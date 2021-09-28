package com.alsritter.gateway.filter;

import com.alsritter.common.RedisConstant;
import com.alsritter.gateway.config.SecureProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;

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
    private final RedisTemplate<String, Object> redisTemplate;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        URI uri = request.getURI();
        PathMatcher pathMatcher = new AntPathMatcher();

        // 白名单路径移除 Token 请求头
        List<String> ignoreUrls = ignoreUrlsConfig.getUrls();

        for (String ignoreUrl : ignoreUrls) {
            if (pathMatcher.match(ignoreUrl, uri.getPath())) {
                request = exchange
                        .getRequest()
                        .mutate()
                        .header("Authorization", "")
                        .build();
                exchange = exchange.mutate().request(request).build();
                return chain.filter(exchange);
            }
        }

        List<Object> publicUrls = redisTemplate.opsForList()
                .range(RedisConstant.RESOURCE_PUBLIC_PERMISSION_LIST, 0, -1);

        if (publicUrls != null) {
            // 如果是访问的公共 URL 直接放行
            for (Object publicUrl : publicUrls) {
                if (pathMatcher.match(publicUrl.toString(), uri.getPath())) {
                    request = exchange
                            .getRequest()
                            .mutate()
                            .header("Authorization", "")
                            .build();
                    exchange = exchange.mutate().request(request).build();
                    return chain.filter(exchange);
                }
            }
        }

        return chain.filter(exchange);
    }
}
