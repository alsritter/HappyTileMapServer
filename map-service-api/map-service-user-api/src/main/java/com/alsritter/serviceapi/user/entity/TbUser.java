package com.alsritter.serviceapi.user.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 用户表(tb_user)实体类
 *
 * @author alsritter
 * @since 2021-06-05 17:00:11
 * @description auto generator
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
	private Long id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码，加密存储
     */
    private String password;
    /**
     * 注册手机号
     */
    private String phone;
    /**
     * 注册邮箱
     */
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
    private LocalDateTime created;
    /**
     * updated
     */
    private LocalDateTime updated;
    /**
     * 0-正常，1-锁定
     */
    private String lockFlag;
    /**
     * 0-正常，1-删除
     */
    private String delFlag;

}