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
     * 根据用户名获取用户信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    @GetMapping(API_PREFIX + "/user-info-by-name")
    SecurityUserDto userInfoByName(@RequestParam("username") String username);

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
}
