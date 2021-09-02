package com.alsritter.gateway.filter;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;

/**
 * 将登录用户的 Token 转成用户信息（从 Redis 里面取得）
 *
 * @author alsritter
 * @version 1.0
 **/
@Slf4j
@Component
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (StrUtil.isEmpty(token)) {
            return chain.filter(exchange);
        }

        // try {
        //     // 从 token 中解析用户信息并设置到 Header 中去
        //     String realToken = token.replace("Bearer ", "");
        //     JWSObject jwsObject = JWSObject.parse(realToken);
        //     String userStr = jwsObject.getPayload().toString();
        //     log.info("AuthGlobalFilter.filter() user:{}", userStr);
        //     ServerHttpRequest request = exchange.getRequest().mutate().header("user", userStr).build();
        //     exchange = exchange.mutate().request(request).build();
        // } catch (ParseException e) {
        //     e.printStackTrace();
        // }

        String realToken = token.replace("Bearer ", "");
        log.info("AuthGlobalFilter.filter() user:{}", realToken);
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
