package com.alsritter.oauth2_2.exception;

import com.alsritter.common.api.CommonResult;
import com.alsritter.common.api.ResultCode;
import com.alsritter.common.exception.BusinessException;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * 全局处理 Oauth2 抛出的异常
 *
 * @author alsritter
 * @version 1.0
 **/
@Slf4j
@RestControllerAdvice // 这里 @RestControllerAdvice等同于 @ControllerAdvice + @ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler({FeignException.class})
    public CommonResult<String> feignExceptionHandler(FeignException exception) {
        log.error(exception.getMessage(), exception);
        return CommonResult.failed(ResultCode.FEIGN_EXCEPTION, exception);
    }

    @ExceptionHandler({InsufficientAuthenticationException.class})
    public CommonResult<String> insufficientAuthenticationExceptionHandler(
            InsufficientAuthenticationException exception) {
        log.error(exception.getMessage(), exception);
        return CommonResult.failed(ResultCode.INSUFFICIENT_AUTHENTICATION_EXCEPTION, exception);
    }

    @ExceptionHandler({UsernameNotFoundException.class})
    public CommonResult<String> handleOauth2(UsernameNotFoundException e) {
        log.error(e.toString());
        return CommonResult.failed(ResultCode.USER_NAME_NOT_FOUND_EXCEPTION, e);
    }

    @ExceptionHandler({OAuth2Exception.class})
    public CommonResult<String> handleOauth2(OAuth2Exception e) {
        log.error(e.toString());
        return CommonResult.failed(ResultCode.OAUTH2_EXCEPTION, e);
    }

    @ExceptionHandler({BusinessException.class})
    public CommonResult<String> handleBusiness(BusinessException e) {
        log.error(e.toString());
        return CommonResult.failed(e.getErrorCode(), e.getMessage());
    }

    @ExceptionHandler({BindException.class})
    public CommonResult<String> bindExceptionHandler(BindException exception) {
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        String errorMessage = fieldErrors.get(0).getDefaultMessage();
        log.error(errorMessage, exception);
        return CommonResult.failed(ResultCode.BIND_EXCEPTION, exception);
    }


    @ExceptionHandler({MethodArgumentNotValidException.class})
    public CommonResult<String> handleBusiness(MethodArgumentNotValidException e) {
        log.error(e.toString());
        return CommonResult.failed(ResultCode.METHOD_ARGUMENT_NOT_VALID_EXCEPTION, e);
    }

    @ExceptionHandler({Exception.class})
    public CommonResult<String> handleBusiness(Exception e) {
        log.error(e.toString());
        return CommonResult.failed(ResultCode.FAILED, e);
    }
}
