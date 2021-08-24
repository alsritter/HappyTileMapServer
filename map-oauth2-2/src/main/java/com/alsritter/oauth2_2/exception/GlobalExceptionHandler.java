package com.alsritter.oauth2_2.exception;

import com.alsritter.common.api.CommonResult;
import com.alsritter.common.exception.BusinessException;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<CommonResult<String>> feignExceptionHandler(FeignException exception) {
        log.error(exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(CommonResult.failed("远距离调用错误"));
    }

    @ExceptionHandler({UsernameNotFoundException.class})
    public ResponseEntity<CommonResult<String>> handleOauth2(UsernameNotFoundException e) {
        log.error(e.toString());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(CommonResult.failed(e.getMessage()));
    }


    @ExceptionHandler({OAuth2Exception.class})
    public ResponseEntity<CommonResult<String>> handleOauth2(OAuth2Exception e) {
        log.error(e.toString());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(CommonResult.failed(e.getMessage()));
    }

    @ExceptionHandler({BusinessException.class})
    public ResponseEntity<CommonResult<String>> handleBusiness(BusinessException e) {
        log.error(e.toString());
        return ResponseEntity.status(e.getErrorCode()).body(CommonResult.failed(e.getMessage()));
    }

    @ExceptionHandler({BindException.class})
    public ResponseEntity<CommonResult<String>> bindExceptionHandler(BindException exception) {
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        String errorMessage = fieldErrors.get(0).getDefaultMessage();
        log.error(errorMessage, exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(CommonResult.failed("参数绑定失败"));
    }


    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<CommonResult<String>> handleBusiness(MethodArgumentNotValidException e) {
        log.error(e.toString());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(CommonResult.failed("请求参数错误"));
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<CommonResult<String>> handleBusiness(Exception e) {
        log.error(e.toString());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(CommonResult.failed(e.getMessage()));
    }
}
