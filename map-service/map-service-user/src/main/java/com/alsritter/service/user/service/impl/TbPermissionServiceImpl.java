package com.alsritter.service.user.service.impl;

import com.alsritter.serviceapi.user.domain.UrlAndNameDo;
import com.alsritter.service.user.mapper.TbPermissionMapper;
import com.alsritter.service.user.mapper.TbRoleMapper;
import com.alsritter.service.user.service.TbPermissionService;
import com.alsritter.serviceapi.user.entity.TbPermission;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 权限表服务接口实现
 *
 * @author alsritter
 * @description 权限
 * @since 2021-06-05 17:00:11
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class TbPermissionServiceImpl extends ServiceImpl<TbPermissionMapper, TbPermission> implements TbPermissionService {
    private final TbPermissionMapper permissionMapper;
    private final TbRoleMapper roleMapper;

    @Cacheable(cacheNames = "allPermission:")
    @Override
    public Map<String, List<String>> getPermission() {
        List<UrlAndNameDo> permissions = permissionMapper.getAllPermissions();

        return permissions.stream()
                .collect(Collectors.groupingBy( UrlAndNameDo::getUrl)).entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().stream().map(UrlAndNameDo::getEnname).collect(Collectors.toList())
                ));
    }

    /**
     * 查询公共资源
     */
    @Cacheable(cacheNames = "allPublicPermission")
    @Override
    public List<String> getPublicPermission() {
        QueryWrapper<TbPermission> wrapper = new QueryWrapper<>();
        wrapper.eq("is_public", 1);
        List<TbPermission> permissions = baseMapper.selectList(wrapper);
        return permissions.stream().map(TbPermission::getUrl).collect(Collectors.toList());
    }
}