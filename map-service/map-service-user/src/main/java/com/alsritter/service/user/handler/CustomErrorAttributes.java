package com.alsritter.service.user.handler;

import com.alsritter.common.exception.BusinessException;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

/**
 * @author kent
 */
@Component
@Primary
public class CustomErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
        Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, options);
        Throwable error = this.getError(webRequest);
        if (error instanceof BusinessException) {
            errorAttributes.put("code", ((BusinessException) error).getErrorCode());
        }
        return errorAttributes;
    }
}
