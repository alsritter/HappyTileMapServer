package com.alsritter.oauth2_2.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.alsritter.common.api.CommonResult;
import com.alsritter.oauth2_2.service.UserService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 注意：这里不能删
 *
 * @author alsritter
 * @version 1.0
 **/
@Slf4j
@Validated
@RestController
@RequestMapping("/handler")
@Setter(onMethod_ = {@Autowired})
public class HandlerController {

    private UserService userService;

    /**
     * 取得图片验证码
     * @return 返回验证码的 Base64
     */
    @GetMapping("/captcha")
    public CommonResult<String> getCaptcha(HttpServletRequest request) {
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(200, 100);
        // 输出 code
        String code = lineCaptcha.getCode();
        //保存验证码到 session
        request.getSession().setAttribute("captcha", code);
        return CommonResult.success(lineCaptcha.getImageBase64());
    }

    /**
     * 设置手机验证码
     */
    @GetMapping("/getPhoneCode")
    public CommonResult<String> getPhoneCode(HttpServletRequest request) {
        //保存验证码到 session
        request.getSession().setAttribute("phoneCode", "1234");
        return CommonResult.success(null);
    }

    /**
     * 设置邮箱验证码
     */
    @GetMapping("/getEmailCode")
    public CommonResult<String> getEmailCode(HttpServletRequest request) {
        //保存验证码到 session
        request.getSession().setAttribute("emailCode", "1234");
        return CommonResult.success(null);
    }

    // @GetMapping("/noLogin")
    // public ResponseEntity<CommonResult<String>> noLogin() {
    //     return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
    //             .body(CommonResult.failed(ResultCode.UNAUTHORIZED, "未登录"));
    // }
    //
    // @GetMapping("/success")
    // public ResponseEntity<CommonResult<SecurityUser>> success() {
    //     SecurityUser user = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    //     //脱敏
    //     user.setUserPassword(null);
    //     return ResponseEntity.ok(CommonResult.success(user));
    // }
    //
    // @GetMapping("/failure")
    // public ResponseEntity<CommonResult<String>> failure() {
    //     return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
    //             .body(CommonResult.failed(ResultCode.FAILED, "登陆失败"));
    // }
}
