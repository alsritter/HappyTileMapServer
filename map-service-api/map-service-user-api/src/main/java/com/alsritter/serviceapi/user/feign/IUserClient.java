package com.alsritter.serviceapi.user.feign;

import com.alsritter.common.AppConstant;
import com.alsritter.common.exception.FeignErrorDecoder;
import com.alsritter.serviceapi.user.domain.SecurityUserDto;
import com.alsritter.serviceapi.user.entity.TbUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(
        value = AppConstant.APPLICATION_USER_NAME,
        fallback = UserClientFallback.class,
        configuration = {FeignErrorDecoder.class}
)
public interface IUserClient {
    String API_PREFIX = "/user";

    /**
     * 获取用户信息
     *
     * @param userId 用户id
     * @return 用户信息
     */
    @GetMapping(API_PREFIX + "/user-info-by-id")
    SecurityUserDto userInfoById(@RequestParam("userId") Long userId);


    /**
     * 根据用户 id 取得 user
     *
     * @param id 用户id
     * @return 用户信息
     */
    @GetMapping(API_PREFIX + "/get-user-by-id")
    TbUser getUser(@RequestParam("id") Long id);

    /**
     * 根据用户名获取用户信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    @GetMapping(API_PREFIX + "/user-info-by-name")
    SecurityUserDto userInfoByName(@RequestParam("username") String username);

    /**
     * 根据邮箱获取用户信息
     *
     * @param email 邮箱
     * @return 用户信息
     */
    @GetMapping(API_PREFIX + "/user-info-by-email")
    SecurityUserDto userInfoByEmail(@RequestParam("email") String email);

    /**
     * 根据手机号获取用户信息
     *
     * @param phone 手机号
     * @return 用户信息
     */
    @GetMapping(API_PREFIX + "/user-info-by-phone")
    SecurityUserDto userInfoByPhone(@RequestParam("phone") String phone);

    /**
     * 新建用户
     *
     * @param user 用户实体
     * @return 用户信息
     */
    @PostMapping(API_PREFIX + "/add-user")
    Boolean addUser(@RequestBody TbUser user);

    /**
     * 取得全部权限
     */
    @GetMapping(API_PREFIX + "/all-permission-by-redis")
    Map<String, List<String>> getPermission();

    /**
     * 取得全部开放资源
     */
    @GetMapping(API_PREFIX + "/all-public-permission-by-redis")
    List<String> getPublicPermission();

    /**
     * 检查是否存在这个 Email
     */
    @GetMapping(API_PREFIX + "/test-email-exist")
    boolean testEmailExist(@RequestParam("email") String email);

    /**
     * 检查是否存在这个 Phone
     */
    @GetMapping(API_PREFIX + "/test-phone-exist")
    boolean testPhoneExist(@RequestParam("phone") String phone);

    /**
     * 设置用户头像
     */
    @PostMapping(API_PREFIX + "/set-user-avatar")
    boolean setUserAvatar(@RequestParam("id") Long id, @RequestParam("url") String url);

    /**
     * 设置新密码
     */
    @PostMapping(API_PREFIX + "/set-new-password")
    boolean setNewPassword(@RequestParam("id") Long id, @RequestParam("password") String password);
}
