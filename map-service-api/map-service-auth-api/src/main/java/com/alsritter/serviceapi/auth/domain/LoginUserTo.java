package com.alsritter.serviceapi.auth.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author alsritter
 * @version 1.0
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(description = "用户登陆")
public class LoginUserTo {
    /**
     * 登陆类型
     */
    @NotBlank
    @ApiModelProperty(value = "登陆类型")
    private String type;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    private String username;

    /**
     * 密码，加密存储
     */
    @ApiModelProperty(value = "密码，加密存储")
    private String password;

    /**
     * 手机号登陆
     */
    @ApiModelProperty(value = "手机号")
    @Pattern(regexp = "^1(3|4|5|7|8)\\d{9}$", message = "手机号码格式错误")
    private String phone;

    /**
     * 邮箱登陆
     */
    @ApiModelProperty(value = "邮箱")
    @Email(message = "邮箱格式错误")
    private String email;

    /**
     * 手机或者邮箱验证码
     */
    @ApiModelProperty(value = "手机或者邮箱验证码")
    private String code;

    /**
     * 验证码
     */
    @ApiModelProperty(value = "验证码")
    @NotBlank
    private String captcha;
}
