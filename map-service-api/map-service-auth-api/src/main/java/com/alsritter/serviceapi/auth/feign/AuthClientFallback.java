package com.alsritter.serviceapi.auth.feign;

import com.alsritter.common.api.ResultCode;
import com.alsritter.common.exception.BusinessException;
import com.alsritter.serviceapi.auth.domain.RegisterUserTo;

/**
 * @author alsritter
 * @version 1.0
 **/
public class AuthClientFallback implements IAuthClient {
    @Override
    public void register(RegisterUserTo user) {
        throw new BusinessException(ResultCode.FEIGN_EXCEPTION);
    }
}
