package com.alsritter.common.token;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Token 的核心在于两个构造方法，一个是认证前使用，一个是认证后使用。
 * 这里把 Token 实体类放在这个公共包是因为资源服务器验证 Token 时需要反序列化这个类
 *
 * @author alsritter
 * @version 1.0
 **/
@Getter
@Setter
@ToString
public class PasswordAuthenticationToken extends AbstractAuthenticationToken {

    //用户信息
    private SecurityUser user;

    /**
     * 用于反序列化的无参构造
     */
    public PasswordAuthenticationToken() {
        super(null);
    }

    /**
     * 注意这个构造方法是认证时使用的
     */
    public PasswordAuthenticationToken(String account, String password) {
        super(null);
        user = new SecurityUser();
        user.setUserAccount(account);
        user.setUserPassword(password);

        super.setAuthenticated(false); //标记未认证
    }

    /**
     * 注意这个构造方法是认证成功后使用的
     */
    public PasswordAuthenticationToken(SecurityUser user, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.user = user;

        super.setAuthenticated(true); //标记已认证
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
