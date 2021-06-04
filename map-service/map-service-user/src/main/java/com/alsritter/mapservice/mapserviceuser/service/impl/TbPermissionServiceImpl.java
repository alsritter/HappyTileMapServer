package com.alsritter.mapservice.mapserviceuser.service.impl;

import com.alsritter.mapservice.mapserviceuser.dao.TbPermissionDao;
import com.alsritter.mapservice.mapserviceuser.service.TbPermissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 权限表服务接口实现
 *
 * @author alsritter
 * @description auto generator
 * @since 2021-06-02 00:37:39
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class TbPermissionServiceImpl implements TbPermissionService {
    private final TbPermissionDao tbPermissionDao;

}