package com.alsritter.service.user.service.impl;

import com.alsritter.common.api.ResultCode;
import com.alsritter.common.exception.BusinessException;
import com.alsritter.service.user.mapper.TbUserMapper;
import com.alsritter.service.user.service.TbRoleService;
import com.alsritter.service.user.service.TbUserService;
import com.alsritter.serviceapi.user.entity.TbUser;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * 用户表服务接口实现
 *
 * @author alsritter
 * @since 2021-06-05 17:00:11
 */
@Service
@RequiredArgsConstructor
public class TbUserServiceImpl extends ServiceImpl<TbUserMapper, TbUser> implements TbUserService {

    private final TbRoleService roleService;

    @Cacheable(cacheNames = "tb_user:", key = "#id")
    @Override
    public TbUser getUserById(long id) {
        TbUser tbUser = new TbUser();
        tbUser.setUserId(id);
        return baseMapper.selectById(tbUser);
    }

    @Override
    @Transactional(rollbackFor = BusinessException.class,
            propagation = Propagation.REQUIRED)
    public void addUser(TbUser user) {
        if (baseMapper.insert(user) == 0) {
            throw new BusinessException("插入用户失败", ResultCode.USER_INSERT_FAILED);
        }

        // 给用户添加权限
        Long userId = user.getUserId();
        roleService.setDefaultRole(userId);
    }

    /**
     * 返回邮箱是否存在，true 表示存在
     */
    @Cacheable(cacheNames = "email:", key = "#email")
    @Override
    public boolean testEmailExist(String email) {
        TbUser tbUser = new TbUser();
        tbUser.setEmail(email);
        Integer count = baseMapper.selectCount(Wrappers.<TbUser>query().lambda().eq(TbUser::getEmail, tbUser.getEmail()));
        count = Optional.ofNullable(count).orElse(0);
        return count != 0;
    }

    /**
     * 返回手机号是否存在，true 表示存在
     */
    @Cacheable(cacheNames = "phone:", key = "#phone")
    @Override
    public boolean testPhoneExist(String phone) {
        TbUser tbUser = new TbUser();
        tbUser.setPhone(phone);
        Integer count = baseMapper.selectCount(Wrappers.<TbUser>query().lambda().eq(TbUser::getPhone, tbUser.getPhone()));
        count = Optional.ofNullable(count).orElse(0);
        return count != 0;
    }

    @Override
    @Transactional(rollbackFor = BusinessException.class,
            propagation = Propagation.REQUIRED)
    public boolean setUserAvatar(Long id, String url) {
        TbUser user = new TbUser();
        user.setAvatar(url);
        int update = baseMapper.update(user, Wrappers.<TbUser>lambdaUpdate().eq(TbUser::getUserId, id));
        if (update < 1) {
            throw new BusinessException(ResultCode.UPDATE_AVATAR_FAILED);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = BusinessException.class,
            propagation = Propagation.REQUIRED)
    public boolean setNewPassword(Long id, String password) {
        TbUser user = new TbUser();
        user.setPassword(password);
        int update = baseMapper.update(user, Wrappers.<TbUser>lambdaUpdate().eq(TbUser::getUserId, id));
        if (update < 1) {
            throw new BusinessException(ResultCode.MODIFY_PASSWORD_FAILED);
        }

        return true;
    }
}