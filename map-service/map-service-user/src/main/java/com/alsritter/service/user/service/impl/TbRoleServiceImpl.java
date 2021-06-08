package com.alsritter.service.user.service.impl;

import com.alsritter.service.user.mapper.TbRoleMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.alsritter.service.user.service.TbRoleService;
import org.springframework.stereotype.Service;

/**
 * 角色表服务接口实现
 *
 * @author alsritter
 * @since 2021-06-05 17:00:11
 * @description auto generator
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class TbRoleServiceImpl implements TbRoleService {
    private final TbRoleMapper tbRoleMapper;

}