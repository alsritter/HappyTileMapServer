package com.alsritter.common.token;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 用户信息
 *
 * @author alsritter
 * @version 1.0
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(description = "用户信息")
public class SecurityUser implements Serializable, UserDetails {
    private static final long serialVersionUID = 1L;
    /**
     * 账号
     */
    @ApiModelProperty(value = "账号")
    private String userAccount;

    /**
     * 密码
     */
    @ApiModelProperty(value = "密码")
    private String userPassword;

    /**
     * 权限标识集合
     */
    @ApiModelProperty(value = "权限集合")
    private List<SimpleGrantedAuthority> permissions;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return permissions;
    }

    @Override
    public String getPassword() {
        return userPassword;
    }

    @Override
    public String getUsername() {
        return userAccount;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
