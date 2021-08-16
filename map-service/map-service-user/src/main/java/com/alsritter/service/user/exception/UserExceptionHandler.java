package com.alsritter.service.user.exception;

import com.alsritter.common.api.CommonResult;
import com.alsritter.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author alsritter
 * @version 1.0
 **/
@Slf4j
@RestControllerAdvice
public class UserExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<CommonResult<String>> handleBusiness(BusinessException e) {
        log.error(e.toString());
        return ResponseEntity.status(e.getErrorCode()).body(CommonResult.failed(e.getMessage()));
    }
}
