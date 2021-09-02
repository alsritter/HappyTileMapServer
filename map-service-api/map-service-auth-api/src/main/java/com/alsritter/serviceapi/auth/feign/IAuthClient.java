package com.alsritter.serviceapi.auth.feign;

import com.alsritter.common.AppConstant;
import com.alsritter.common.exception.FeignErrorDecoder;
import com.alsritter.serviceapi.auth.domain.RegisterUserTo;
import com.alsritter.serviceapi.auth.domain.UserInfoVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotBlank;

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

    /**
     * 根据 token 取得用户信息
     */
    @GetMapping("/getUserInfoByToken")
    UserInfoVO getUserInfoByToken(@NotBlank @RequestParam("token") String accessToken);

    /**
     * 检查密码是否正确
     */
    @GetMapping(API_PREFIX + "/check-user-password")
    boolean checkPassword(@RequestParam("id") Long id,@RequestParam("password") String password);

    /**
     * 设置新密码
     */
    @PostMapping(API_PREFIX + "/set-new-password")
    boolean setNewPassword(@RequestParam("id") Long id, @RequestParam("password") String password);
}
