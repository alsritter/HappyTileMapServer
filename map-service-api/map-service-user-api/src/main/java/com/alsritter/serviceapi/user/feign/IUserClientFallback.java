package com.alsritter.serviceapi.user.feign;

import com.alsritter.serviceapi.user.domain.SecurityUserDto;
import com.alsritter.serviceapi.user.entity.TbUser;
import org.springframework.http.ResponseEntity;

/**
 * Feign 失败配置
 *
 * @author alsritter
 * @version 1.0
 **/
public class IUserClientFallback implements IUserClient {

    @Override
    public ResponseEntity<SecurityUserDto> userInfoById(Long userId) {
        // "未获取到账号信息"
        return ResponseEntity.badRequest().body(null);
    }

    @Override
    public ResponseEntity<SecurityUserDto> userInfoByName(String username) {
        return ResponseEntity.badRequest().body(null);
    }

    @Override
    public ResponseEntity<Boolean> addUser(TbUser user) {
        return ResponseEntity.badRequest().body(null);
    }
}
