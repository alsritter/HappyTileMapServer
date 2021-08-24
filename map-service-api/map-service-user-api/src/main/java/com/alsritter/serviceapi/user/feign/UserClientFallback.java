package com.alsritter.serviceapi.user.feign;

import com.alsritter.common.api.ResultCode;
import com.alsritter.common.exception.BusinessException;
import com.alsritter.serviceapi.user.domain.SecurityUserDto;
import com.alsritter.serviceapi.user.entity.TbUser;

import java.util.List;
import java.util.Map;

/**
 * Feign 失败配置
 *
 * @author alsritter
 * @version 1.0
 **/
public class UserClientFallback implements IUserClient {

    @Override
    public SecurityUserDto userInfoById(Long userId) {
        // "未获取到账号信息"
        throw new BusinessException(ResultCode.READ_TIME_OUT_EXCEPTION);
    }

    @Override
    public SecurityUserDto userInfoByName(String username) {
        throw new BusinessException(ResultCode.READ_TIME_OUT_EXCEPTION);
    }

    @Override
    public Boolean addUser(TbUser user) {
        throw new BusinessException(ResultCode.READ_TIME_OUT_EXCEPTION);
    }

    @Override
    public Map<String, List<String>> getPermission() {
        throw new BusinessException(ResultCode.READ_TIME_OUT_EXCEPTION);
    }
}
