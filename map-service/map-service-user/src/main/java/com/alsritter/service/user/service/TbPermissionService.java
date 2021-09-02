package com.alsritter.service.user.service;


import java.util.List;
import java.util.Map;

/**
 * 权限表服务接口
 *
 * @author alsritter
 * @description auto generator
 * @since 2021-06-05 17:00:11
 */
public interface TbPermissionService {

    /**
     * 取得全部权限
     */
    Map<String, List<String>> getPermission();

    List<String> getPublicPermission();
}
