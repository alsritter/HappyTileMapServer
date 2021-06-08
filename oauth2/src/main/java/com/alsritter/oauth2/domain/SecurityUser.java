package com.alsritter.oauth2.domain;


import com.alsritter.serviceapi.user.entity.TbUser;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
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
@ApiModel(description = "用户信息")
public class SecurityUser implements Serializable, UserDetails {
    private static final long serialVersionUID = 1L;

    /**
     * 用户基础信息
     */
    @ApiModelProperty(value = "用户")
    private TbUser user;

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
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
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
