package com.alsritter.service.user.service.impl;

import com.alsritter.common.api.ResultCode;
import com.alsritter.common.exception.BusinessException;
import com.alsritter.service.user.mapper.TbRoleMapper;
import com.alsritter.service.user.service.TbRoleService;
import com.alsritter.serviceapi.user.entity.TbRole;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 角色表服务接口实现
 *
 * @author alsritter
 * @description auto generator
 * @since 2021-06-05 17:00:11
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class TbRoleServiceImpl extends ServiceImpl<TbRoleMapper, TbRole> implements TbRoleService {
    private final TbRoleMapper tbRoleMapper;

    @Override
    @Transactional(rollbackFor = BusinessException.class,
            propagation = Propagation.REQUIRED)
    public void setRole(long userId, long roleId) {
        TbRole tbRole = baseMapper.selectById(roleId);
        if (tbRole == null) throw new BusinessException(ResultCode.SET_ROLE_FAILED, "不存在这个 Role");

        if (tbRoleMapper.existCheck(userId, roleId) != 0) {
            throw new BusinessException(ResultCode.SET_ROLE_FAILED, "当前用户已经存在这个权限");
        }

        if (tbRoleMapper.addRoleByUserId(userId, roleId) == 0) {
            throw new BusinessException(ResultCode.SET_ROLE_FAILED);
        }
    }

    @Override
    @Transactional(rollbackFor = BusinessException.class,
            propagation = Propagation.REQUIRED)
    public void setDefaultRole(long userId) {
        if (tbRoleMapper.existCheck(userId, 2L) != 0) {
            throw new BusinessException(ResultCode.SET_ROLE_FAILED, "当前用户已经存在这个权限");
        }

        if (tbRoleMapper.addRoleByUserId(userId, 2L) == 0) {
            throw new BusinessException(ResultCode.SET_ROLE_FAILED, "给用户添加默认权限失败");
        }
    }
}