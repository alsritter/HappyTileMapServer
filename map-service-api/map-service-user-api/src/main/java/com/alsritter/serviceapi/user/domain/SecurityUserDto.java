package com.alsritter.serviceapi.user.domain;

import com.alsritter.serviceapi.user.entity.TbPermission;
import com.alsritter.serviceapi.user.entity.TbRole;
import com.alsritter.serviceapi.user.entity.TbUser;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 用来传输对象信息
 *
 * @author alsritter
 * @version 1.0
 **/
@Data
public class SecurityUserDto implements Serializable {
    private List<TbPermission> permission;
    private List<TbRole> roles;
    private TbUser user;
}
