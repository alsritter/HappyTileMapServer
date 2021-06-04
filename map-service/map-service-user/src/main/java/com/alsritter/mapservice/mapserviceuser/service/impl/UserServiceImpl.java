package com.alsritter.mapservice.mapserviceuser.service.impl;

import com.alsritter.mapservice.mapserviceuser.dao.TbUserDao;
import com.alsritter.mapservice.mapserviceuser.mapper.TbUserMapper;
import com.alsritter.mapservice.mapserviceuser.service.UserService;
import com.alsritter.mapserviceapi.mapserviceuserapi.entity.TbUser;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 用户表服务接口实现
 *
 * @author alsritter
 * @description auto generator
 * @since 2021-06-02 00:37:39
 */
@Slf4j
@Service
@Setter(onMethod_ = {@Autowired})
public class UserServiceImpl implements UserService {

    private TbUserDao tbUserDao;

    private TbUserMapper userMapper;
    private PasswordEncoder passwordEncoder;

    //根据 账号查询用户信息
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userMapper.getUserByUsername(username);
    }

    @Override
    public void registerUser(String username, String password) {

        String passwd = passwordEncoder.encode(password);

        // 这里随便注册一个用户
        TbUser tbUser = new TbUser();

        tbUser.setUsername(username);
        tbUser.setPassword(passwd);

        tbUserDao.save(tbUser);
    }
}