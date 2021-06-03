package com.alsritter.mapuaa.service.impl;

import com.alsritter.mapuaa.dao.TbUserDao;
import com.alsritter.mapuaa.entity.TbUser;
import com.alsritter.mapuaa.mapper.TbUserMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import com.alsritter.mapuaa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * 用户表服务接口实现
 *
 * @author alsritter
 * @since 2021-06-02 00:37:39
 * @description auto generator
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
        QueryWrapper<TbUser> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username); // 根据用户名查询

        TbUser tbUser = userMapper.selectOne(wrapper);

        // 先随便创建一个空权限
        tbUser.setPermissions(new ArrayList<>());
        return tbUser;
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