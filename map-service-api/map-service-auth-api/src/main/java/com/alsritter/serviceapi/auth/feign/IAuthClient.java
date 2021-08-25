package com.alsritter.serviceapi.auth.feign;

import com.alsritter.common.AppConstant;
import com.alsritter.common.exception.FeignErrorDecoder;
import com.alsritter.serviceapi.auth.domain.RegisterUserTo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(
        value = AppConstant.APPLICATION_OAUTH2_NAME,
        fallback = AuthClientFallback.class,
        configuration = {FeignErrorDecoder.class}
)
public interface IAuthClient {
    String API_PREFIX = "/login";

    /**
     * 注册用户
     */
    @PostMapping("/register")
    void register(RegisterUserTo user);
}
