package com.alsritter.service.user.feign;

import com.alsritter.common.api.ResultCode;
import com.alsritter.common.exception.BusinessException;
import com.alsritter.service.user.service.SecurityUserService;
import com.alsritter.service.user.service.TbPermissionService;
import com.alsritter.service.user.service.TbUserService;
import com.alsritter.serviceapi.user.domain.SecurityUserDto;
import com.alsritter.serviceapi.user.entity.TbUser;
import com.alsritter.serviceapi.user.feign.IUserClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 这里只能使用 @RestController 注解，不能使用 @Component 注解
 * @author alsritter
 * @version 1.0
 **/
@Slf4j
@RestController
@AllArgsConstructor
public class UserClient implements IUserClient {

    private final TbPermissionService permissionService;
    private final SecurityUserService securityUserService;
    private final TbUserService userService;

    @Override
    public SecurityUserDto userInfoById(Long userId) {
        log.info("查询的 ID 是{}", userId);
        return securityUserService.getUserInfoById(userId);
    }

    @Override
    public SecurityUserDto userInfoByName(String username) {
        log.info("查询的 username 是{}", username);
        return securityUserService.getUserInfoByName(username);
    }

    @Override
    public Boolean addUser(TbUser user) {
        try {
            userService.addUser(user);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new BusinessException(ResultCode.USER_INSERT_FAILED);
        }
        return true;
    }

    @Override
    public Map<String, List<String>> getPermission() {
        return permissionService.getPermission();
    }
}
