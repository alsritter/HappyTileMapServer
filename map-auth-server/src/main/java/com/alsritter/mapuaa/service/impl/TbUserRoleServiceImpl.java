package com.alsritter.mapuaa.service.impl;

import com.alsritter.mapuaa.dao.TbUserRoleDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.alsritter.mapuaa.service.TbUserRoleService;
import org.springframework.stereotype.Service;

/**
 * 用户角色表服务接口实现
 *
 * @author alsritter
 * @since 2021-06-02 00:37:39
 * @description auto generator
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class TbUserRoleServiceImpl implements TbUserRoleService {
    private final TbUserRoleDao tbUserRoleDao;

}