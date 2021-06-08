package com.alsritter.oauth2.config;

import com.alsritter.oauth2.provider.Provider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * filter 用于接收网络请求，调用 manager 进行认证
 * manager 管理多个 provider，选择合适的 provider 进行认证
 * provider 负责认证，返回 token
 * token 存储认证信息
 *
 * @author alsritter
 * @version 1.0
 **/
public class AuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {

    /**
     * 认证url
     */
    protected AuthenticationProcessingFilter() {
        super("/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse httpServletResponse)
            throws AuthenticationException, IOException, ServletException {
        String type = request.getParameter("type");

        //策略模式
        Provider provider = new Provider(type);
        return getAuthenticationManager().authenticate(provider.executeStrategy(request));
    }
}
