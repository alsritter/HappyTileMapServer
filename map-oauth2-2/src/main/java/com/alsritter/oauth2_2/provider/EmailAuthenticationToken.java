package com.alsritter.oauth2_2.provider;

import com.alsritter.oauth2_2.domain.SecurityUser;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @author alsritter
 * @version 1.0
 **/
@Getter
public class EmailAuthenticationToken extends AbstractAuthenticationToken {
    //用户信息
    private SecurityUser user;
    private String email;

    private String code;
    private String realCode;


    /**
     * 注意这个构造方法是认证时使用的
     */
    public EmailAuthenticationToken(String email, String code, String realCode) {
        super(null);
        this.email = email;
        this.code = code;
        this.realCode = realCode;

        //标记未认证
        super.setAuthenticated(false);
    }

    /**
     * 注意这个构造方法是认证成功后使用的
     */
    public EmailAuthenticationToken(SecurityUser user, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.user = user;

        //标记已认证
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    /**
     * @return 获取用户信息
     */
    @Override
    public Object getPrincipal() {
        return user;
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
