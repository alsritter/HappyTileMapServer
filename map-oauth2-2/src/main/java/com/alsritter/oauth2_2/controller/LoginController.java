package com.alsritter.oauth2_2.controller;

import com.alsritter.common.api.CommonResult;
import com.alsritter.common.api.ResultCode;
import com.alsritter.oauth2_2.domain.RegisterUserTo;
import com.alsritter.common.token.SecurityUser;
import com.alsritter.oauth2_2.service.UserService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author alsritter
 * @version 1.0
 **/
@Slf4j
@Validated
@RestController
@RequestMapping("/login")
@Setter(onMethod_ = {@Autowired})
public class LoginController {

    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<CommonResult<String>> register(@RequestBody @Validated RegisterUserTo user) {

        try {
            userService.registerUser(user);
        } catch (Exception e) {
            log.warn(e.getMessage());
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                    .body(CommonResult.failed(ResultCode.FAILED, "注册失败"));
        }
        return ResponseEntity.ok(CommonResult.success("注册成功"));
    }

    @GetMapping("/noLogin")
    public ResponseEntity<CommonResult<String>> noLogin() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(CommonResult.failed(ResultCode.UNAUTHORIZED, "未登录"));
    }

    @GetMapping("/success")
    public ResponseEntity<CommonResult<SecurityUser>> success() {
        SecurityUser user = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //脱敏
        user.setUserPassword(null);
        return ResponseEntity.ok(CommonResult.success(user));
    }

    @GetMapping("/failure")
    public ResponseEntity<CommonResult<String>> failure() {
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                .body(CommonResult.failed(ResultCode.FAILED, "登陆失败"));
    }

    @GetMapping("/getPhoneCode")
    public ResponseEntity<CommonResult<String>> getPhoneCode(HttpServletRequest request) {
        //保存验证码到 session
        request.getSession().setAttribute("phoneCode", "1234");
        return ResponseEntity.ok(CommonResult.success("1234"));
    }

    @GetMapping("/getEmailCode")
    public ResponseEntity<CommonResult<String>> getEmailCode(HttpServletRequest request) {
        //保存验证码到session
        request.getSession().setAttribute("emailCode", "1234");
        return ResponseEntity.ok(CommonResult.success("1234"));
    }
}