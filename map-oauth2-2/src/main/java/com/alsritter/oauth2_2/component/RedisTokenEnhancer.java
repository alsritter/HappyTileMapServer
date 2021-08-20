package com.alsritter.oauth2_2.component;

import cn.hutool.core.lang.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.common.*;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

/**
 * TokenEnhancer 是令牌增强器
 *
 * @author alsritter
 * @version 1.0
 **/
@Slf4j
@Component
public class RedisTokenEnhancer implements TokenEnhancer {

    /**
     * 返回一个增强后的 Token
     *
     * @param accessToken    需要增强的 Token
     * @param authentication 在 Security 那里取得的 Authentication
     * @return 增强后的 Token
     */
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        log.debug("返回的 Token 类型为： {}", accessToken.getTokenType());
        if (accessToken instanceof DefaultOAuth2AccessToken) {
            DefaultOAuth2AccessToken token = ((DefaultOAuth2AccessToken) accessToken);
            token.setValue(getNewToken());
            //判断 refresh_token 对象是否是 ExpiringOAuth2RefreshToken 的实例对象，这一段是设置 refresh_token 的关键
            OAuth2RefreshToken refreshToken = token.getRefreshToken();
            if (refreshToken instanceof ExpiringOAuth2RefreshToken) {
                // 不需要让 RefreshToken 存那么多信息，这里给它一个随机字符串就行了
                token.setRefreshToken(new DefaultOAuth2RefreshToken(getNewToken()));
            }
            return token;
        }
        return accessToken;
    }

    private String getNewToken() {
        return UUID.randomUUID().toString().replace("-", "");
    }

}
