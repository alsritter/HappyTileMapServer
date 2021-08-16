package com.alsritter.service.user.service.impl;

import com.alsritter.common.api.ResultCode;
import com.alsritter.common.exception.BusinessException;
import com.alsritter.service.user.mapper.TbUserMapper;
import com.alsritter.service.user.service.TbUserService;
import com.alsritter.serviceapi.user.entity.TbUser;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户表服务接口实现
 *
 * @author alsritter
 * @since 2021-06-05 17:00:11
 */
@Service
public class TbUserServiceImpl extends ServiceImpl<TbUserMapper, TbUser> implements TbUserService {
    @Override
    public TbUser getUserById(long id) {
        TbUser tbUser = new TbUser();
        tbUser.setUserId(id);
        return baseMapper.selectById(tbUser);
    }

    @Override
    @Transactional(rollbackFor = BusinessException.class)
    public void addUser(TbUser user) {
        if (baseMapper.insert(user) == 0) {
            throw new BusinessException("插入用户失败", ResultCode.USER_INSERT_FAILED);
        }
    }
}