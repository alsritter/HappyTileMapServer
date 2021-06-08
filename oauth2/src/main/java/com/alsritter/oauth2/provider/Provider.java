package com.alsritter.oauth2.provider;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

/**
 * @author alsritter
 * @version 1.0
 **/
public class Provider {
    private ProviderStrategy providerStrategy;

    /**
     * 根据 type 选择 provider
     *
     * @param type 登陆的类型
     */
    public Provider(String type) {

        //默认密码登录，如果传入一个不存在的类型，就使用密码登录
        switch (type) {
            case "phone":
                this.providerStrategy = new PhoneAuthenticationProvider();
                break;
            case "email":
                this.providerStrategy = new EmailAuthenticationProvider();
                break;
            default:
                this.providerStrategy = new PasswordAuthenticationProvider();
                break;
        }
    }

    /**
     * 执行某 provider 的认证方法
     *
     * @param request
     * @return
     */
    public Authentication executeStrategy(HttpServletRequest request) {
        return providerStrategy.authenticate(request);
    }
}
