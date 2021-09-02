package com.alsritter.service.user.service;


import com.alsritter.serviceapi.user.entity.TbUser;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 用户表服务接口
 *
 * @author alsritter
 * @description auto generator
 * @since 2021-06-05 17:00:11
 */
public interface TbUserService {
    TbUser getUserById(long id);


    void addUser(TbUser user);

    /**
     * 检查是否存在这个 Email
     */
    boolean testEmailExist(String email);

    /**
     * 检查是否存在这个 Phone
     */
    boolean testPhoneExist(String phone);

    /**
     * 修改用户头像
     */
    boolean setUserAvatar(Long id, String url);

    /**
     * 修改新密码
     */
    boolean setNewPassword(Long id, String password);
}
