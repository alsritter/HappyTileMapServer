package com.alsritter.service.user.service.impl;

import com.alsritter.service.user.mapper.TbPermissionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.alsritter.service.user.service.TbPermissionService;
import org.springframework.stereotype.Service;

/**
 * 权限表服务接口实现
 *
 * @author alsritter
 * @since 2021-06-05 17:00:11
 * @description auto generator
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class TbPermissionServiceImpl implements TbPermissionService {
    private final TbPermissionMapper tbPermissionMapper;

}