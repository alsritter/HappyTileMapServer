package com.alsritter.service.user.service.impl;

import com.alsritter.service.user.mapper.TbUserMapper;
import com.alsritter.service.user.service.TbUserService;
import com.alsritter.serviceapi.user.entity.TbUser;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 用户表服务接口实现
 *
 * @author alsritter
 * @description auto generator
 * @since 2021-06-05 17:00:11
 */
@Service
public class TbUserServiceImpl extends ServiceImpl<TbUserMapper, TbUser> implements TbUserService {
    @Override
    public TbUser getUserById(long id) {
        TbUser tbUser = new TbUser();
        tbUser.setId(id);
        return baseMapper.selectById(tbUser);
    }
}