package com.alsritter.serviceapi.user.entity;

import com.alsritter.serviceapi.user.enums.GenderEnum;
import com.alsritter.serviceapi.user.enums.StatusEnum;
import com.alsritter.serviceapi.user.enums.lockFlagEnum;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户表(tb_user)实体类
 *
 * @author alsritter
 * @description auto generator
 * @since 2021-06-05 17:00:11
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("tb_user")
public class TbUser extends Model<TbUser> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    @NotBlank
    private Long userId;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码，加密存储
     */
    private String password;
    /**
     * 0-女，1-男, 2-保密
     */
    private GenderEnum gender;
    /**
     * 注册手机号
     */
    @Pattern(regexp = "^1(3|4|5|7|8)\\d{9}$", message = "手机号码格式错误")
    private String phone;
    /**
     * 注册邮箱
     */
    @Email(message = "邮箱格式错误")
    private String email;
    /**
     * 头像地址
     */
    private String avatar;
    /**
     * 个人信息
     */
    private String description;
    /**
     * created
     */
    private LocalDateTime createTime;
    /**
     * updated
     */
    private LocalDateTime lastModifyTime;
    /**
     * 0-正常，1-不可以评论，2-不可以登录
     */
    private StatusEnum status;
    /**
     * 0-正常，1-锁定
     */
    private lockFlagEnum lockFlag;

}