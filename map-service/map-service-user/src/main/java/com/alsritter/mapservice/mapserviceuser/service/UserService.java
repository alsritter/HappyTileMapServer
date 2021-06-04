package com.alsritter.mapservice.mapserviceuser.service;


import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * 用户表服务接口
 *
 * @author alsritter
 * @since 2021-06-02 00:37:39
 * @description auto generator
 */
public interface UserService extends UserDetailsService {
    void registerUser(String username, String password) ;
}
