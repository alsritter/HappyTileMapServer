package com.alsritter.common.exception;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alsritter.common.api.CommonResult;
import com.alsritter.common.api.ResultCode;
import feign.FeignException;
import feign.Response;
import feign.RetryableException;
import feign.codec.ErrorDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

/**
 * @author alsritter
 * @version 1.0
 **/
public class FeignErrorDecoder extends ErrorDecoder.Default {
    static final Logger log = LoggerFactory.getLogger(FeignErrorDecoder.class);

    @Override
    public Exception decode(String methodKey, Response response) {
        Exception exception = super.decode(methodKey, response);

        // 如果是 RetryableException，则返回继续重试
        if (exception instanceof RetryableException) {
            return exception;
        }

        try {
            // 如果是 FeignException，则对其进行处理，并抛出 BusinessException
            if (exception instanceof FeignException &&
                    ((FeignException) exception).responseBody().isPresent()) {
                ByteBuffer responseBody = ((FeignException) exception).responseBody().get();
                String body = StandardCharsets.UTF_8.newDecoder()
                        .decode(responseBody.asReadOnlyBuffer()).toString();

                log.error(body);

                JSONObject jsonObject = new JSONObject(body);
                //
                //
                // // 将异常信息，转换为 CommonResult 对象
                // CommonResult exceptionInfo = JSONUtil.toBean(bodyText, CommonResult.class);
                // 如果 exception 中 code 不为空，则使用该 code，否则使用默认的错误code
                Integer code = Optional
                        .ofNullable(jsonObject.getInt("status"))
                        .orElse(ResultCode.FAILED.getCode());
                // 如果 exception 中 message 不为空，则使用该 message，否则使用默认的错误message
                String message = Optional
                        .ofNullable(jsonObject.getStr("error"))
                        .orElse(ResultCode.FAILED.getMessage());

                return new BusinessException(code, message);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
        return exception;
    }
}
