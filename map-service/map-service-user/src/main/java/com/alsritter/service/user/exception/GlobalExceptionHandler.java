package com.alsritter.service.user.exception;

import com.alsritter.common.api.CommonResult;
import com.alsritter.common.api.ResultCode;
import com.alsritter.common.exception.BusinessException;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * 全局处理 Oauth2 抛出的异常
 * 因为 Feign，所以错误要抛状态码
 *
 * @author alsritter
 * @version 1.0
 **/
@Slf4j
@RestControllerAdvice // 这里 @RestControllerAdvice等同于 @ControllerAdvice + @ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler({FeignException.class})
    public ResponseEntity<CommonResult<String>> feignExceptionHandler(FeignException exception) {
        log.warn(exception.getMessage(), exception);
        exception.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(CommonResult.failed(ResultCode.FEIGN_EXCEPTION, exception));
    }

    /**
     * 角色凭证错误
     */
    @ExceptionHandler({InsufficientAuthenticationException.class})
    public ResponseEntity<CommonResult<String>> insufficientAuthenticationExceptionHandler(
            InsufficientAuthenticationException exception) {
        log.warn(exception.getMessage(), exception);
        exception.printStackTrace();
        return
                ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(CommonResult.failed(ResultCode.INSUFFICIENT_AUTHENTICATION_EXCEPTION, exception));
    }


    @ExceptionHandler({UsernameNotFoundException.class})
    public ResponseEntity<CommonResult<String>> handleOauth2(UsernameNotFoundException exception) {
        log.error(exception.toString());
        exception.printStackTrace();
        return
                ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(CommonResult.failed(ResultCode.USER_NAME_NOT_FOUND_EXCEPTION, exception));
    }


    @ExceptionHandler({BusinessException.class})
    public ResponseEntity<CommonResult<String>> handleBusiness(BusinessException exception) {
        log.error(exception.toString());
        exception.printStackTrace();
        return
                ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(CommonResult.failed(exception.getErrorCode(), exception.getMessage()));
    }

    @ExceptionHandler({BindException.class})
    public ResponseEntity<CommonResult<String>> bindExceptionHandler(BindException exception) {
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        String errorMessage = fieldErrors.get(0).getDefaultMessage();
        log.error(errorMessage, exception);
        exception.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(CommonResult.failed(ResultCode.BIND_EXCEPTION, exception));
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<CommonResult<String>> handleBusiness(MethodArgumentNotValidException exception) {
        log.error(exception.toString());
        exception.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(CommonResult.failed(ResultCode.REQUEST_PARAMETER_ERROR, exception));
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<CommonResult<String>> handleBusiness(Exception exception) {
        log.error(exception.toString());
        exception.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(CommonResult.failed(ResultCode.FAILED, exception));
    }
}
