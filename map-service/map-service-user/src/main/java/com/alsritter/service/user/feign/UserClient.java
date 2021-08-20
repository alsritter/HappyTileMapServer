package com.alsritter.service.user.feign;

import com.alsritter.common.api.ResultCode;
import com.alsritter.common.exception.BusinessException;
import com.alsritter.service.user.service.SecurityUserService;
import com.alsritter.service.user.service.TbUserService;
import com.alsritter.serviceapi.user.domain.SecurityUserDto;
import com.alsritter.serviceapi.user.entity.TbUser;
import com.alsritter.serviceapi.user.feign.IUserClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author alsritter
 * @version 1.0
 **/
@Slf4j
@RestController
@AllArgsConstructor
public class UserClient implements IUserClient {

    private final SecurityUserService securityUserService;
    private final TbUserService userService;

    @Override
    public ResponseEntity<SecurityUserDto> userInfoById(Long userId) {
        log.info("查询的 ID 是{}", userId);
        return ResponseEntity.ok(securityUserService.getUserInfoById(userId));
    }

    @Override
    public ResponseEntity<SecurityUserDto> userInfoByName(String username) {
        log.info("查询的 username 是{}", username);
        return ResponseEntity.ok(securityUserService.getUserInfoByName(username));
    }

    @Override
    public ResponseEntity<Boolean> addUser(TbUser user) {
        try {
            userService.addUser(user);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new BusinessException(ResultCode.USER_INSERT_FAILED);
        }
        return ResponseEntity.ok(true);
    }
}
