package com.alsritter.oauth2_2.provider;

import com.alsritter.oauth2_2.domain.SecurityUser;
import com.alsritter.oauth2_2.service.UserService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.http.HttpServletRequest;

/**
 * 这里主要是用来认证用户是否是正常的
 * 其实 SpringSecurity 默认提供了一个 DaoAuthenticationProvider 去加载用户
 * <p>
 * 例如在这个 WebSecurityConfig 配置的这行
 * auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
 *
 * @author alsritter
 * @version 1.0
 **/
@Setter(onMethod_ = {@Autowired})
public class PasswordAuthenticationProvider implements AuthenticationProvider, ProviderStrategy {

    private UserService userService;
    private PasswordEncoder passwordEncoder;


    /**
     * 认证代码，认证通过返回认证对象，失败返回 null
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        PasswordAuthenticationToken token = (PasswordAuthenticationToken) authentication;

        if (token.getUser() == null) return null;

        //验证账号密码
        SecurityUser user = userService.getUserByName(token.getUser().getUsername());

        if (user != null) {
            //加密密码的比较
            if (!passwordEncoder.matches(token.getUser().getUserPassword(), user.getPassword())) {
                return null;
            }

            //写入用户信息并返回认证类
            return new PasswordAuthenticationToken(user, user.getAuthorities());
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
        String user = request.getParameter("username");
        String password = request.getParameter("password");
        return new PasswordAuthenticationToken(user, password);
    }

    /**
     * Manager 传递 token 给 provider 时会调用本方法判断该 provider 是否支持该 token。
     * 不支持则尝试下一个 filter
     * 本类支持的 token 类： UserPasswordAuthenticationToken
     *
     * @return 当前 provider 是否支持该 token。
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return (PasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
