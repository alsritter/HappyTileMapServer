package com.alsritter.service.user.service.impl;

import com.alsritter.service.user.mapper.TbPermissionMapper;
import com.alsritter.service.user.mapper.TbRoleMapper;
import com.alsritter.service.user.mapper.TbUserMapper;
import com.alsritter.service.user.service.SecurityUserService;
import com.alsritter.serviceapi.user.domain.SecurityUserDto;
import com.alsritter.serviceapi.user.entity.TbUser;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 用来返回 OAuth 需要的数据
 *
 * @author alsritter
 * @version 1.0
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class SecurityUserServiceImpl extends ServiceImpl<TbUserMapper, TbUser> implements SecurityUserService {
    private final TbPermissionMapper permissionMapper;
    private final TbRoleMapper roleMapper;


    @Override
    public SecurityUserDto getUserInfoById(long id) {
        TbUser tbUser = new TbUser();
        tbUser.setUserId(id);
        tbUser = baseMapper.selectById(tbUser);
        return getUserInfo(tbUser);
    }

    @Override
    public SecurityUserDto getUserInfoByName(String username) {
        TbUser tbUser = new TbUser();
        tbUser.setUsername(username);
        tbUser = baseMapper.selectOne(Wrappers.<TbUser>query().lambda().eq(TbUser::getUsername, tbUser.getUsername()));
        return getUserInfo(tbUser);
    }

    private SecurityUserDto getUserInfo(TbUser tbUser) {
        SecurityUserDto userInfo = new SecurityUserDto();
        userInfo.setUser(tbUser);
        // userInfo.setPermissions(permissionsByUserId.stream().map(x -> new SimpleGrantedAuthority(x.getEnname())).collect(Collectors.toList()));
        userInfo.setPermission(permissionMapper.findPermissionsByUserId(tbUser.getUserId()));
        userInfo.setRoles(roleMapper.findRoleByUserId(tbUser.getUserId()));
        return userInfo;
    }
}
