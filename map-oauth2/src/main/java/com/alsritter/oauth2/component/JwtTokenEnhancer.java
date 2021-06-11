package com.alsritter.oauth2.component;

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
public class JwtTokenEnhancer implements TokenEnhancer {

    /**
     * 返回一个增强后的 Token
     * 这个在这个 Token 里面添加一些信息（HashMap 里面的内容）
     * <p>
     * 这个方法会在用户调用 /oauth/token 时调用
     * 注意，这里的 getTokenType 方法的类型是前端传的 grant_type
     * <p>
     * 自定义 refresh_token 参考自：
     * Spring Security OAuth2 Provider 之 自定义开发
     * https://www.cnblogs.com/panchanggui/p/12275578.html
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
            // val user = (SecurityUser) authentication.getPrincipal();
            // user.getUser().setPassword(null);
            // // 只有这个 access token 里面存了信息
            // token.setValue(JSONUtil.toJsonStr(user.getPermissions()));

            token.setValue(getNewToken());

            //判断 refresh_token 对象是否是 ExpiringOAuth2RefreshToken 的实例对象，这一段是设置 refresh_token 的关键
            OAuth2RefreshToken refreshToken = token.getRefreshToken();

            if (refreshToken instanceof ExpiringOAuth2RefreshToken) {
                // 不需要让 RefreshToken 存那么多信息，这里给它一个随机字符串就行了
                token.setRefreshToken(new DefaultOAuth2RefreshToken(getNewToken()));
            }


            // 令牌授予者想要添加到令牌的附加信息，例如支持新的令牌类型。
            // 不过要注意，这里是全局添加的
            // Map<String, Object> additionalInformation = new HashMap<>();
            // additionalInformation.put("user", user.getUser());
            // token.setAdditionalInformation(additionalInformation);

            return token;
        }
        return accessToken;
    }

    private String getNewToken() {
        return UUID.randomUUID().toString().replace("-", "");
    }

}
