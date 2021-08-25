package com.alsritter.oauth2_2.fegin;

import com.alsritter.common.exception.BusinessException;
import com.alsritter.oauth2_2.service.UserService;
import com.alsritter.serviceapi.auth.domain.RegisterUserTo;
import com.alsritter.serviceapi.auth.feign.IAuthClient;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author alsritter
 * @version 1.0
 **/
@Slf4j
@RestController
@Setter(onMethod_ = {@Autowired})
public class AuthClient implements IAuthClient {

    private UserService userService;

    @Override
    public void register(@RequestBody @Validated RegisterUserTo user) {
        try {
            userService.registerUser(user);
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw new BusinessException("注册失败", e);
        }
    }
}
