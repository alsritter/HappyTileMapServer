package com.alsritter.service.forum.config;

import com.alsritter.serviceapi.auth.domain.UserInfoVO;
import com.alsritter.serviceapi.auth.feign.IAuthClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 对携带 Token 的请求进行拦截，取得用户信息
 * 在拦截器中使用 Feign 有 Bug
 * 具体参考：
 * Spring Interceptor 自动注入FeignClient导致循环依赖2.0
 * https://juejin.cn/post/6844904001285128199#heading-11
 *
 * @author alsritter
 * @version 1.0
 **/
public class SecurityInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private IAuthClient authClient;

    /**
     * 这个方法在 Controller 处理请求之前被调用
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String accessToken = request.getHeader("Authorization");
        if (accessToken != null) {
            accessToken = accessToken.replaceFirst("Bearer ", "");
            UserInfoVO user = authClient.getUserInfoByToken(accessToken);
            UserContext.setUser(user);
        }
        return true;
    }

    /**
     * 该方法将在整个请求结束之后执行
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        UserContext.clear();
    }
}
