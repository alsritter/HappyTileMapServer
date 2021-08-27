package com.alsritter.service.forum.api;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.alsritter.common.AppConstant;
import com.alsritter.common.AuthConstant;
import com.alsritter.common.api.CommonResult;
import com.alsritter.common.api.ResultCode;
import com.alsritter.common.exception.BusinessException;
import com.alsritter.service.forum.config.MyRemoteCallProperties;
import com.alsritter.serviceapi.auth.domain.LoginUserTo;
import com.alsritter.serviceapi.auth.domain.RegisterUserTo;
import com.alsritter.serviceapi.auth.feign.IAuthClient;
import com.alsritter.serviceapi.user.feign.IUserClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

/**
 * @author alsritter
 * @version 1.0
 **/
@Slf4j
@Validated
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthApi {

    private final IAuthClient authClient;
    private final IUserClient userClient;
    private final RestTemplate restTemplate;
    private final MyRemoteCallProperties.ClientWeb remoteCallProperties;

    /**
     * 取得用户的登陆态
     */
    @GetMapping("/getuser")
    public CommonResult<String> getUser() {
        return CommonResult.success("ok");
    }

    /**
     * 登陆
     */
    @PostMapping(value = "/login", produces = "application/json;charset=utf-8")
    public String login(HttpServletRequest request, @RequestBody @Validated LoginUserTo user) {

        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        List<String> cookieList = getCookieList(request);
        // 需要转发 Cookie
        headers.put("Cookie", cookieList);
        // 下面开始构造请求
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        headers.add("Authorization", "Basic " + remoteCallProperties.getBase64());
        // 设置请求参数
        MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();
        postParameters.add("type", user.getType());
        postParameters.add("grant_type", "password");

        switch (user.getType()) {
            case "phone":
                String phone = user.getPhone();
                String code = user.getCode();

                if (phone == null || code == null) throw new BusinessException(ResultCode.REQUEST_PARAMETER_ERROR);

                postParameters.add("phone", phone);
                postParameters.add("code", code);
                break;
            case "email":
                String email = user.getEmail();
                String code2 = user.getCode();

                if (email == null || code2 == null) throw new BusinessException(ResultCode.REQUEST_PARAMETER_ERROR);

                postParameters.add("email", email);
                postParameters.add("code", code2);
                break;
            default:
                if (user.getCaptcha() == null) throw new BusinessException(
                        ResultCode.CAPTCHA_EXCEPTION, "请输入验证码");

                Object captcha = request.getSession().getAttribute("captcha");
                Object timeout = request.getSession().getAttribute("captchaTimeout");

                if (timeout == null) {
                    throw new BusinessException(
                            ResultCode.CAPTCHA_EXCEPTION,
                            "请重新获取验证码");
                }

                // 获取超时时间
                long captchaTimeout = Long.parseLong(timeout.toString());
                if (captcha == null ||
                        captchaTimeout < System.currentTimeMillis()) throw new BusinessException(
                        ResultCode.CAPTCHA_EXCEPTION,
                        "请重新获取验证码");

                String captchaCode = captcha.toString();

                log.debug(captchaCode);

                if (!captchaCode.equals(user.getCaptcha())) throw new BusinessException(
                        ResultCode.CAPTCHA_EXCEPTION,
                        "验证码输入错误");

                String username = user.getUsername();
                String password = user.getPassword();

                if (username == null || password == null)
                    throw new BusinessException(ResultCode.REQUEST_PARAMETER_ERROR);

                postParameters.add("username", username);
                postParameters.add("password", password);
                break;
        }

        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(postParameters, headers);
        String json = restTemplate.postForObject("http://" + AppConstant.APPLICATION_OAUTH2_NAME + "/login", httpEntity, String.class);
        // 销毁这个验证码
        request.getSession().setAttribute("captcha", "");
        return json;
    }

    /**
     * 注册需要校验邮箱地址
     */
    @PostMapping(value = "/register", produces = "application/json;charset=utf-8")
    public String register(HttpServletRequest request, @RequestBody @Validated RegisterUserTo user) {
        String realCode = (String) request.getSession().getAttribute("emailCode");

        Object timeout = request.getSession().getAttribute("emailCodeTimeout");

        if (timeout == null) {
            throw new BusinessException(
                    ResultCode.EMAIL_CAPTCHA_EXCEPTION,
                    "请重新获取验证码");
        }

        // 获取超时时间
        long codeTimeout = Long.parseLong(timeout.toString());

        if (realCode == null ||
                codeTimeout < System.currentTimeMillis()) throw new BusinessException(
                ResultCode.EMAIL_CAPTCHA_EXCEPTION,
                "请重新获取验证码");

        log.debug(realCode);

        if (!realCode.equals(user.getCode())) throw new BusinessException(
                ResultCode.EMAIL_CAPTCHA_EXCEPTION,
                "验证码输入错误");

        authClient.register(user);

        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        headers.add("Authorization", "Basic " + remoteCallProperties.getBase64());

        // 设置请求参数
        MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();
        postParameters.add("type", "password");
        postParameters.add("grant_type", "password");
        postParameters.add("username", user.getUsername());
        postParameters.add("password", user.getPassword());
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(postParameters, headers);

        String json = restTemplate.postForObject("http://" + AppConstant.APPLICATION_OAUTH2_NAME + "/login", httpEntity, String.class);
        // 销毁这个验证码
        request.getSession().setAttribute("emailCode", "");
        return json;
    }

    /**
     * 刷新令牌
     */
    @GetMapping(value = "/refresh_token", produces = "application/json;charset=utf-8")
    public String refreshToken(@RequestParam("refresh_token") @NotBlank String token) {
        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");

        // 设置请求参数
        MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();
        postParameters.add("grant_type", "refresh_token");
        postParameters.add("refresh_token", token);
        postParameters.add("client_id", remoteCallProperties.getClientId());
        postParameters.add("client_secret", remoteCallProperties.getClientSecret());
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(postParameters, headers);

        return restTemplate.postForObject("http://" + AppConstant.APPLICATION_OAUTH2_NAME + "/oauth/token", httpEntity, String.class);
    }


    /**
     * 取得图片验证码
     *
     * @return 返回验证码的 Base64
     */
    @GetMapping("/captcha")
    public CommonResult<String> getCaptcha(HttpServletRequest request) {
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(230, 50);
        // 输出 code
        String code = lineCaptcha.getCode();
        log.debug(code);
        // 保存验证码到 session
        request.getSession().setAttribute("captcha", code);
        // 设置过期时间
        request.getSession().setAttribute("captchaTimeout",
                System.currentTimeMillis() + AuthConstant.TIMEOUT_TIME);
        return CommonResult.success(lineCaptcha.getImageBase64());
    }


    @GetMapping("/testPhoneExist")
    public CommonResult<Boolean> testPhoneExist(@Pattern(regexp = "^1(3|4|5|7|8)\\d{9}$", message = "手机号码格式错误")
                                                @NotBlank
                                                @RequestParam("phone") String phone) {
        return CommonResult.success(userClient.testPhoneExist(phone));
    }

    @GetMapping("/testEmailExist")
    public CommonResult<Boolean> testEmailExist(@Email(message = "邮箱格式错误")
                                                @NotBlank
                                                @RequestParam("email") String email) {
        return CommonResult.success(userClient.testEmailExist(email));
    }

    /**
     * 取得 Cookie List
     */
    public List<String> getCookieList(HttpServletRequest request) {
        List<String> cookieList = new ArrayList<>();

        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) {
            return cookieList;
        }

        for (Cookie cookie : cookies) {
            cookieList.add(cookie.getName() + "=" + cookie.getValue());
        }
        return cookieList;
    }
}
