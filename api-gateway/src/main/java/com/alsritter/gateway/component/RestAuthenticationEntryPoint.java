package com.alsritter.gateway.component;

import cn.hutool.json.JSONUtil;
import com.alsritter.common.api.CommonResult;
import com.alsritter.common.api.ResultCode;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.http.AccessTokenRequiredException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * 自定义返回结果：没有登录或 token 过期时
 *
 * @author alsritter
 * @version 1.0
 **/
@Component
public class RestAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {
    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException e) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.OK);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        String body = null;
        // 打印一下错误
        e.printStackTrace();

        if (e instanceof AccountExpiredException) {
            body = JSONUtil.toJsonStr(CommonResult.failed(ResultCode.ACCOUNT_EXPIRED));
        }
        else if (e instanceof AccessTokenRequiredException) {
            body = JSONUtil.toJsonStr(CommonResult.failed(ResultCode.ACCESS_REQUIRED_EXCEPTION));
        }
        else if (e instanceof AccountStatusException) {
            body = JSONUtil.toJsonStr(CommonResult.failed(ResultCode.ACCOUNT_STATUS_EXCEPTION));
        }
        else {
            body = JSONUtil.toJsonStr(CommonResult.unauthorized(e.getMessage()));
        }


        DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer));
    }
}

