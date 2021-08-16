package com.alsritter.oauth2_2.config;

import cn.hutool.core.util.StrUtil;
import com.alsritter.oauth2_2.provider.Provider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.exceptions.InvalidRequestException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * filter 用于接收网络请求，调用 manager 进行认证
 * manager 管理多个 provider，选择合适的 provider 进行认证
 * provider 负责认证，检查账号密码之类的，返回 token
 * token 存储认证信息，包含账号密码，具体可以自己定义
 *
 * @author alsritter
 * @version 1.0
 **/
public class AuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {

    /**
     * 设置默认的认证 url 地址
     */
    protected AuthenticationProcessingFilter() {
        super("/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse httpServletResponse)
            throws AuthenticationException, IOException, ServletException {
        // 从前端取得 type 参数
        String type = request.getParameter("type");

        if (StrUtil.isEmpty(type)) {
            throw new InvalidRequestException("Missing login type");
        }

        // 策略模式
        Provider provider = new Provider(type);
        return getAuthenticationManager().authenticate(provider.executeStrategy(request));
    }
}
