package com.alsritter.oauth2_2.filters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 全局统一异常处理只能处理控制器中发生的异常。
 * 要在 Spring Security 过滤器链中重用此功能，
 * 需要定义过滤器并将其挂钩到安全配置中。
 * 过滤器需要将异常重定向到统一异常处理中。
 * <p>
 * Reference
 * https://blog.csdn.net/not_say/article/details/105654454
 *
 * @author alsritter
 * @version 1.0
 **/
@Component
public class FilterChainExceptionHandler extends OncePerRequestFilter {
    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            resolver.resolveException(request, response, null, e);
        }
    }
}
