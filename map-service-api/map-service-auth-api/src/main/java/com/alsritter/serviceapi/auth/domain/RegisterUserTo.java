package com.alsritter.serviceapi.auth.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 前端传过来的用户注册信息
 *
 * @author alsritter
 * @version 1.0
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(description = "用户注册")
public class RegisterUserTo {
    /**
     * 用户名
     */
    @NotBlank
    @ApiModelProperty(value = "用户名")
    private String username;
    /**
     * 密码，加密存储
     */
    @NotBlank
    @ApiModelProperty(value = "密码，加密存储")
    private String password;
    /**
     * 注册手机号
     */
    @ApiModelProperty(value = "注册手机号")
    @NotBlank
    @Pattern(regexp = "^1(3|4|5|7|8)\\d{9}$", message = "手机号码格式错误")
    private String phone;
    /**
     * 注册邮箱（邮箱不能为空，因为需要根据邮箱登陆）
     */
    @ApiModelProperty(value = "注册邮箱")
    @NotBlank
    @Email(message = "邮箱格式错误")
    private String email;
    /**
     * 邮箱验证码
     */
    @NotBlank
    @ApiModelProperty(value = "邮箱验证码")
    private String code;
    // /**
    //  * 头像地址
    //  */
    // @Pattern(regexp = "(ht|f)tp(s?)\\:\\/\\/[0-9a-zA-Z]([-.\\w]*[0-9a-zA-Z])*(:(0-9)*)*(\\/?)([a-zA-Z0-9\\-\\.\\?\\,\\'\\/\\\\&%\\+\\$#_=]*)?",
    //         message = "头像地址错误")
    // @ApiModelProperty(value = "头像地址")
    // private String avatar;
    /**
     * 个人信息
     */
    @ApiModelProperty(value = "个人信息")
    private String description;

}
