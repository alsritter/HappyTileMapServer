package com.alsritter.oauth2.exception;

import com.alsritter.common.api.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局处理 Oauth2 抛出的异常
 *
 * @author alsritter
 * @version 1.0
 **/
@Slf4j
@RestControllerAdvice // 这里 @RestControllerAdvice等同于 @ControllerAdvice + @ResponseBody
public class Oauth2ExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<CommonResult<String>> handleOauth2(OAuth2Exception e) {
        log.error(e.toString());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(CommonResult.failed(e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<CommonResult<String>> handleOauth2(UsernameNotFoundException e) {
        log.error(e.toString());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(CommonResult.failed(e.getMessage()));
    }
}
