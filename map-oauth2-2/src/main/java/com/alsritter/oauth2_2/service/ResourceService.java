package com.alsritter.oauth2_2.service;

import com.alsritter.common.RedisConstant;
import com.alsritter.serviceapi.user.feign.IUserClient;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

/**
 * 创建一个资源服务 ResourceServiceImpl，初始化的时候把资源与角色匹配关系缓存到 Redis 中，
 * 方便网关服务进行鉴权的时候获取。
 *
 * @author alsritter
 * @version 1.0
 **/
@Service
@RequiredArgsConstructor
public class ResourceService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final IUserClient userClient;

    @PostConstruct
    public void initData() {
        // 先清空原本 Redis 里面的数据
        redisTemplate.delete(RedisConstant.RESOURCE_ROLES_MAP);
        redisTemplate.delete(RedisConstant.RESOURCE_PUBLIC_PERMISSION_LIST);

        Map<String, List<String>> resourceRolesMap = userClient.getPermission();
        List<String> publicPermission = userClient.getPublicPermission();
        // Map<String, List<String>> resourceRolesMap = new HashMap<>();
        // resourceRolesMap.put("/api/hello", CollUtil.toList("ADMIN"));
        // resourceRolesMap.put("/api/user/currentUser", CollUtil.toList("ADMIN", "TEST"));
        // resourceRolesMap.put("/discovery/instances", CollUtil.toList("ADMIN", "TEST", "USER"));
        redisTemplate.opsForHash().putAll(RedisConstant.RESOURCE_ROLES_MAP, resourceRolesMap);
        // leftPushAll 有个坑，所以必须传入数组，否则默认传的是一个整体
        redisTemplate.opsForList().leftPushAll(RedisConstant.RESOURCE_PUBLIC_PERMISSION_LIST, publicPermission.toArray(new String[0]));

    }
}
