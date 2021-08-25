package com.alsritter.oauth2_2.controller;

import com.alsritter.common.RedisConstant;
import com.alsritter.common.api.CommonResult;
import com.alsritter.serviceapi.user.feign.IUserClient;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 控制访问权限
 *
 * @author alsritter
 * @version 1.0
 **/
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/permission")
@Setter(onMethod_ = {@Autowired})
public class PermissionController {
    private final RedisTemplate<String, Object> redisTemplate;
    private final IUserClient permissionsClient;

    @PostMapping("/refresh")
    public ResponseEntity<CommonResult<String>> refreshPermission() {
        // 先清空原本 Redis 里面的数据
        redisTemplate.delete(RedisConstant.RESOURCE_ROLES_MAP);
        Map<String, List<String>> resourceRolesMap = permissionsClient.getPermission();
        redisTemplate.opsForHash().putAll(RedisConstant.RESOURCE_ROLES_MAP, resourceRolesMap);
        return ResponseEntity.ok(CommonResult.success("刷新成功"));
    }
}
