package com.alsritter.service.user.service;

import com.alsritter.serviceapi.user.domain.SecurityUserDto;

/**
 * 用来返回 OAuth 需要的数据
 *
 * @author alsritter
 * @since 2021-06-05 17:00:11
 */
public interface SecurityUserService {
    SecurityUserDto getUserInfoById(long id);
    SecurityUserDto getUserInfoByName(String username);
    SecurityUserDto getUserInfoByEmail(String email);
    SecurityUserDto getUserInfoByPhone(String phone);
}
