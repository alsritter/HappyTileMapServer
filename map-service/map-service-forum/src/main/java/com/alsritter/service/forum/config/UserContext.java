package com.alsritter.service.forum.config;

import com.alsritter.common.api.ResultCode;
import com.alsritter.common.exception.BusinessException;
import com.alsritter.serviceapi.auth.domain.UserInfoVO;
import lombok.extern.slf4j.Slf4j;

/**
 * 用于取得用户对象的静态工具类
 *
 * @author alsritter
 * @version 1.0
 **/
@Slf4j
public class UserContext {

    // 构造方法私有化
    private UserContext(){}

    /**
     * 登录用户上下文存储
     */
    private static final ThreadLocal<UserInfoVO> context = new ThreadLocal<>();

    /**
     * 清除当前线程内引用，防止内存泄漏
     */
    public static void clear() {
        context.remove();
    }

    /**
     * 获取用户信息
     * @return UserInfoVO
     */
    public static UserInfoVO getUser() {
        UserInfoVO user = context.get();
        if (user == null) {
            context.remove();
            throw new BusinessException(ResultCode.ACCOUNT_GET_USER_INFO_EXCEPTION);
        }
        return user;
    }

    /**
     * 存放用户信息
     */
    public static void setUser(UserInfoVO user) {
        context.set(user);
    }
}
