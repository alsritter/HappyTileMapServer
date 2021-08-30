package com.alsritter.oauth2_2.controller;

import com.alsritter.common.api.ResultCode;
import com.alsritter.common.exception.BusinessException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 自定义的检查端点
 * 参考自
 * {@link org.springframework.security.oauth2.provider.endpoint.CheckTokenEndpoint}
 * 不用担心与自带的检查端点冲突，它会自动覆盖掉
 *
 * @author alsritter
 * @version 1.0
 **/
@RestController
public class CustomCheckTokenEndpoint {
    private final ResourceServerTokenServices resourceServerTokenServices;
    private final AccessTokenConverter accessTokenConverter = new DefaultAccessTokenConverter();

    public CustomCheckTokenEndpoint(ResourceServerTokenServices resourceServerTokenServices) {
        this.resourceServerTokenServices = resourceServerTokenServices;
    }

    @RequestMapping(value = "/oauth/check_token")
    @ResponseBody
    public Map<String, ?> checkToken(@RequestParam("token") String value) {

        OAuth2AccessToken token = resourceServerTokenServices.readAccessToken(value);
        // 主要修改这里，因为 Redis 存储 Token 的策略是直接 Key 过期，所以为空时直接抛出超时
        if (token == null || token.isExpired()) {
            throw new BusinessException(ResultCode.ACCOUNT_EXPIRED);
        }

        OAuth2Authentication authentication = resourceServerTokenServices.loadAuthentication(token.getValue());
        Map<String, Object> response = (Map<String, Object>) accessTokenConverter.convertAccessToken(token, authentication);

        // gh-1070
        response.put("active", true);    // Always true if token exists and not expired

        return response;
    }
}
