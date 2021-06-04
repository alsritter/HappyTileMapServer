package com.alsritter.mapservice.mapserviceuser.service.impl;

import com.alsritter.mapservice.mapserviceuser.dao.TbRoleDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.alsritter.mapservice.mapserviceuser.service.TbRoleService;
import org.springframework.stereotype.Service;

/**
 * 角色表服务接口实现
 *
 * @author alsritter
 * @since 2021-06-02 00:37:39
 * @description auto generator
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class TbRoleServiceImpl implements TbRoleService {
    private final TbRoleDao tbRoleDao;
}