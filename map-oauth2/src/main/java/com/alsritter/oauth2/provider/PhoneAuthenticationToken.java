package com.alsritter.oauth2.provider;

import com.alsritter.oauth2.domain.SecurityUser;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 自定义一个 AuthenticationToken 对象，对标 UsernamePasswordAuthenticationToken 这个实现类
 * 被认证后的 AuthenticationToken 会被填充到 SecurityContextHolder 安全上下文容器中
 *
 * @author alsritter
 * @version 1.0
 **/
@Getter
public class PhoneAuthenticationToken extends AbstractAuthenticationToken {

    //用户信息
    private SecurityUser user;
    private String phone;

    // 用户输入的验证码
    private String code;
    // 真正的验证码
    private String realCode;

    /**
     * 注意这个构造方法是认证时使用的
     */
    public PhoneAuthenticationToken(String phone, String code, String realCode) {
        super(null);
        this.phone = phone;
        this.code = code;
        this.realCode = realCode;

        super.setAuthenticated(false); //标记未认证
    }

    /**
     * 注意这个构造方法是认证成功后使用的
     */
    public PhoneAuthenticationToken(SecurityUser user, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.user = user;

        super.setAuthenticated(true); //标记已认证
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    /**
     * 获取用户信息
     */
    @Override
    public Object getPrincipal() {
        return this.user;
    }


    /**
     * 这里的重写单纯就是为了抑制警告
     */
    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
