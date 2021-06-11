package com.alsritter.oauth2.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
    @ApiModelProperty(value = "用户名")
    private String username;
    /**
     * 密码，加密存储
     */
    @ApiModelProperty(value = "密码，加密存储")
    private String password;
    /**
     * 注册手机号
     */
    @ApiModelProperty(value = "注册手机号")
    private String phone;
    /**
     * 注册邮箱
     */
    @ApiModelProperty(value = "注册邮箱")
    private String email;
    /**
     * 头像地址
     */
    @ApiModelProperty(value = "头像地址")
    private String avatar;
    /**
     * 个人信息
     */
    @ApiModelProperty(value = "个人信息")
    private String description;
}
