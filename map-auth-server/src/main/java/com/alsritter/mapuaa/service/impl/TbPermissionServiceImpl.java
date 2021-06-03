package com.alsritter.mapuaa.service.impl;

import com.alsritter.mapuaa.dao.TbPermissionDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.alsritter.mapuaa.service.TbPermissionService;
import org.springframework.stereotype.Service;

/**
 * 权限表服务接口实现
 *
 * @author alsritter
 * @since 2021-06-02 00:37:39
 * @description auto generator
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class TbPermissionServiceImpl implements TbPermissionService {
    private final TbPermissionDao tbPermissionDao;

}