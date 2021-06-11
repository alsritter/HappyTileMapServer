package com.alsritter.oauth2.provider;

import com.alsritter.oauth2.domain.SecurityUser;
import com.alsritter.oauth2.service.UserService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.http.HttpServletRequest;

/**
 * @author alsritter
 * @version 1.0
 **/
@Setter(onMethod_ = {@Autowired})
public class EmailAuthenticationProvider implements AuthenticationProvider, ProviderStrategy {

    private UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        //认证代码，认证通过返回认证对象，失败返回null
        EmailAuthenticationToken token = (EmailAuthenticationToken) authentication;
        if (token.getEmail() == null || token.getCode() == null || token.getRealCode() == null) {
            return null;
        }

        //检查验证码
        if (!token.getCode().equals(token.getRealCode())) {
            return null;
        }

        SecurityUser user = userService.getUserByEmail(token.getEmail());

        if (user != null) {
            // 写入用户信息并返回认证类
            return new PhoneAuthenticationToken(user, user.getAuthorities());
        }

        return null;
    }

    /**
     * 从请求中取得登陆的参数
     *
     * @return 返回一个待认证的 Token
     */
    @Override
    public Authentication authenticate(HttpServletRequest request) {
        String email = request.getParameter("email");
        String code = request.getParameter("code");

        String realCode = (String) request.getSession().getAttribute("emailCode");
        return new EmailAuthenticationToken(email, code, realCode);
    }


    /**
     * Manager 传递 token 给 provider 时会调用本方法判断该 provider 是否支持该 token。
     * 不支持则尝试下一个 filter
     * 本类支持的 token 类： EmailAuthenticationToken
     *
     * @return 当前 provider 是否支持该 token。
     */
    @Override
    public boolean supports(Class<?> authentication) {
        boolean assignableFrom = EmailAuthenticationToken.class.isAssignableFrom(authentication);
        return assignableFrom;
    }
}
