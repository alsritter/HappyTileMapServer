package com.alsritter.service.forum.api;

import com.alsritter.common.api.CommonResult;
import com.alsritter.common.api.ResultCode;
import com.alsritter.common.exception.BusinessException;
import com.alsritter.service.forum.config.UserContext;
import com.alsritter.serviceapi.auth.domain.UserInfoVO;
import com.alsritter.serviceapi.auth.feign.IAuthClient;
import com.alsritter.serviceapi.user.feign.IUserClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author alsritter
 * @version 1.0
 **/
@Slf4j
@Validated
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserApi {
    private final IUserClient userClient;
    private final IAuthClient authClient;


    /**
     * 根据 Token 取得用户的登陆态
     * 这个 @Validated 对 @RequestParam 是无效的
     */
    @GetMapping("/getuser")
    public CommonResult<UserInfoVO> getUser(@RequestParam(required = true) String token) {
        // 对于公共的 API 不能使用 UserContext.getUser() 取得 Token
        return CommonResult.success(authClient.getUserInfoByToken(token));
    }

    /**
     * 上传头像
     */
    @PostMapping("/setavatar")
    public CommonResult<String> uploadAvatar(@NotBlank
                                             @Pattern(regexp = "(ht|f)tp(s?)\\:\\/\\/[0-9a-zA-Z]([-.\\w]*[0-9a-zA-Z])*(:(0-9)*)*(\\/?)([a-zA-Z0-9\\-\\.\\?\\,\\'\\/\\\\&%\\+\\$#_=]*)?",
                                                     message = "头像地址错误")
                                                     String avatarUrl) {
        UserInfoVO user = UserContext.getUser();
        if (user.getUserId() == null) {
            throw new BusinessException(ResultCode.ACCOUNT_GET_USER_INFO_EXCEPTION);
        }
        userClient.setUserAvatar(user.getUserId(), avatarUrl);
        return CommonResult.success("上传成功");
    }

    @PostMapping("/mod/pwd")
    public CommonResult<String> modPwd(@NotBlank String oldPassword,
                                       @NotBlank String newPassword) {
        UserInfoVO user = UserContext.getUser();
        // 先检查旧的密码
        if (authClient.checkPassword(user.getUserId(), oldPassword)) {
            throw new BusinessException(ResultCode.PASSWORD_ERROR);
        }
        // 没问题了再更新密码
        authClient.setNewPassword(user.getUserId(), newPassword);
        return CommonResult.success("密码更新成功");
    }
}
