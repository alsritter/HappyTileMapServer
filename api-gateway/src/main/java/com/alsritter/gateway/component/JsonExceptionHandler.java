package com.alsritter.gateway.component;

import com.alsritter.common.api.ResultCode;
import com.alsritter.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author alsritter
 * @version 1.0
 **/
@Slf4j
public class JsonExceptionHandler extends DefaultErrorWebExceptionHandler {

    public JsonExceptionHandler(ErrorAttributes errorAttributes, ResourceProperties resourceProperties,
                                ErrorProperties errorProperties, ApplicationContext applicationContext) {
        super(errorAttributes, resourceProperties, errorProperties, applicationContext);
    }

    /**
     * get error attributes
     */
    @Override
    protected Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Throwable error = super.getError(request);
        Map<String, Object> map = new HashMap<>(3);
        if (error instanceof BusinessException) {
            map.put("code", ((BusinessException) error).getErrorCode());
            map.put("message", error.getMessage());
            map.put("data", null);

        } else {
            String exMessage = error != null ? error.getMessage() : ResultCode.FAILED.getMessage();
            String message = String.format("request error [%s %s]，exception：%s", request.methodName(), request.uri(), exMessage);
            map.put("code", ResultCode.FAILED.getCode());
            map.put("message", message);
            map.put("data", null);
        }
        return map;
    }

    /**
     * render with json
     *
     * @param errorAttributes
     */
    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    /**
     * response http code 200
     * the error code need use the code in response content
     *
     * @param errorAttributes
     */
    @Override
    protected int getHttpStatus(Map<String, Object> errorAttributes) {
        return HttpStatus.OK.value();
    }
}

