package com.alsritter.gateway.component;

import com.alsritter.common.RedisConstant;
import com.alsritter.gateway.config.SecureProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.server.PathContainer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 主要用来动态匹配哪些链接无需走过滤链
 *
 * @author alsritter
 * @version 1.0
 **/
@Component
@RequiredArgsConstructor
public class CustomServerWebExchangeMatcher implements ServerWebExchangeMatcher {
    private final RedisTemplate<String, Object> redisTemplate;
    private final SecureProperties.IgnoreUrl ignoreUrlsConfig;



    @Override
    public Mono<MatchResult> matches(ServerWebExchange exchange) {

        ServerHttpRequest request = exchange.getRequest();
        PathContainer path = request.getPath().pathWithinApplication();
        AntPathMatcher pathMatcher = new AntPathMatcher();
        for (String ignoreUrl : ignoreUrlsConfig.getUrls()) {
            if (pathMatcher.match(ignoreUrl, path.value())) {
                return MatchResult.match();
            }
        }

        List<Object> publicUrls = redisTemplate.opsForList()
                .range(RedisConstant.RESOURCE_PUBLIC_PERMISSION_LIST, 0, -1);

        if (publicUrls != null) {
            // 如果是访问的公共 URL 直接放行
            // if (publicUrl.contains(path.value())) {
            //     return MatchResult.match();
            // }
            for (Object publicUrl : publicUrls) {
                if (pathMatcher.match(publicUrl.toString(), path.value())) {
                    return MatchResult.match();
                }
            }
        }

        return MatchResult.notMatch();
    }
}
