package com.alsritter.oauth2_2.provider;

import com.alsritter.common.exception.BusinessException;
import com.alsritter.common.token.PhoneAuthenticationToken;
import com.alsritter.common.token.SecurityUser;
import com.alsritter.oauth2_2.service.UserService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.http.HttpServletRequest;

/**
 * 编写一个自定的 AuthenticationProvider
 *
 * @author alsritter
 * @version 1.0
 **/
@Setter(onMethod_ = {@Autowired})
public class PhoneAuthenticationProvider implements AuthenticationProvider, ProviderStrategy {

    private UserService userService;


    /**
     * 从请求中取得登陆的参数
     *
     * @return 返回一个待认证的 Token
     */
    @Override
    public Authentication authenticate(HttpServletRequest request) {
        String phone = request.getParameter("phone");
        String code = request.getParameter("code");

        String realCode = (String) request.getSession().getAttribute("phoneCode");
        Object timeout = request.getSession().getAttribute("phoneCodeTimeout");

        if (timeout == null || Long.parseLong(timeout.toString()) < System.currentTimeMillis()) {
            throw new BusinessException("请重新发送验证码");
        }

        return new PhoneAuthenticationToken(phone, code, realCode);
    }


    /**
     * 认证代码，认证通过返回认证对象，失败返回 null
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        PhoneAuthenticationToken token = (PhoneAuthenticationToken) authentication;
        if (token.getPhone() == null || token.getCode() == null || token.getRealCode() == null) {
            return null;
        }

        //检查验证码
        if (!token.getCode().equals(token.getRealCode())) {
            return null;
        }

        //根据手机号获取用户信息
        SecurityUser user = userService.getUserPhone(token.getPhone());

        if (user != null) {
            // 写入用户信息并返回认证类
            return new PhoneAuthenticationToken(user, user.getAuthorities());
        }

        return null;
    }


    /**
     * Manager 传递 token 给 provider 时会调用本方法判断该 provider 是否支持该 token。
     * 不支持则尝试下一个 filter
     * 本类支持的 token 类： PhoneAuthenticationToken
     *
     * @return 当前 provider 是否支持该 token。
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return PhoneAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
