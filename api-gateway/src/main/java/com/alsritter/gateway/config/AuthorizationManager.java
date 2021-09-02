package com.alsritter.gateway.config;

import cn.hutool.core.convert.Convert;
import com.alsritter.common.AuthConstant;
import com.alsritter.common.RedisConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * 鉴权管理器，用于判断是否有资源的访问权限
 * <p>
 * 注意：Zuul 网关导致请求头信息丢失的解决办法
 * https://blog.csdn.net/czx2018/article/details/105618223
 *
 * @author alsritter
 * @version 1.0
 **/
@RequiredArgsConstructor
@Component
public class AuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> mono, AuthorizationContext authorizationContext) {
        URI uri = authorizationContext.getExchange().getRequest().getURI();
        String path = uri.getPath();

        //从 Redis 中获取当前路径可访问角色列表
        Object obj = redisTemplate.opsForHash().get(RedisConstant.RESOURCE_ROLES_MAP, path);

        final List<String> authorities = Convert.toList(String.class, obj).stream()
                .map(i -> i = (AuthConstant.AUTHORITY_PREFIX + i)
                        .toUpperCase(Locale.ROOT))
                .collect(Collectors.toList());

        //认证通过且角色匹配的用户可访问当前路径
        return mono
                .filter(Authentication::isAuthenticated)
                .flatMapIterable(Authentication::getAuthorities)
                .map(GrantedAuthority::getAuthority)
                .any(o -> "SCOPE_ALL".equals(o) || authorities.contains(AuthConstant.AUTHORITY_PREFIX + o))
                // .any(authorities::contains)
                .map(AuthorizationDecision::new)
                .defaultIfEmpty(new AuthorizationDecision(false));
    }
}
