package com.alsritter.serviceapi.auth.domain;

import com.alsritter.serviceapi.user.enums.GenderEnum;
import com.alsritter.serviceapi.user.enums.StatusEnum;
import com.alsritter.serviceapi.user.enums.lockFlagEnum;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 返回给前端的角色信息
 *
 * @author alsritter
 * @version 1.0
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoVO {
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
     * 0-女，1-男, 2-保密
     */
    private GenderEnum gender;
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

    public Map<String, String> buildMap() {
        Map<String, String> map = new HashMap<>();
        map.put("userId", userId.toString());
        map.put("username", username);
        map.put("gender", gender.toString());
        map.put("phone", phone);
        map.put("email", email);
        map.put("avatar", avatar);
        map.put("description", description);
        map.put("createTime", createTime.toString());
        map.put("lastModifyTime", lastModifyTime.toString());
        map.put("status", status.toString());
        map.put("lockFlag", lockFlag.toString());
        return map;
    }
}
