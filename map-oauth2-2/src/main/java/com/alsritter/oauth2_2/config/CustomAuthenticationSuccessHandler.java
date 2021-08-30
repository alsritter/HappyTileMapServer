package com.alsritter.oauth2_2.config;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidRequestException;
import org.springframework.security.oauth2.common.exceptions.UnapprovedClientAuthenticationException;
import org.springframework.security.oauth2.common.exceptions.UnsupportedGrantTypeException;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * 总结：就是在这里生成 Token 的
 *
 *
 * SpringSecurity 默认有 Form登录和 Basic登录，在 WebSecurityConfigurerAdapter 类的 configure 方法上面设置了 http.formLogin() 也就是表单登录，
 * 也就是这里的用户名密码登录，默认情况下 SpringSecurity 已经实现了表单登录的封装了，所以只要设置成功之后返回的 Token 就好，
 * 创建一个继承 SavedRequestAwareAuthenticationSuccessHandler 的 CustomAuthenticationSuccessHandler类，代码如下：
 * <p>
 * 参考资料：前后端分离项目 — 基于SpringSecurity OAuth2.0用户认证
 * https://segmentfault.com/a/1190000016583573
 *
 * @author alsritter
 * @version 1.0
 **/
@Slf4j
@Component
@Setter(onMethod_ = {@Autowired})
public class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private ObjectMapper objectMapper;
    private ClientDetailsService clientDetailsService;

    /**
     * 这里通过名字指定从 AuthorizationServerConfig 配置类里面配置的 DefaultTokenServices
     * 注意 @Autowired 默认是按照类型装配注入的，如果想按照名称来转配注入，则需要结合 @Qualifier 一起使用；
     * 注意：access_token 从哪里来 参考自：
     * Spring Boot+OAuth2，如何自定义返回的 Token 信息？
     * https://blog.csdn.net/u012702547/article/details/105804972
     */
    @Qualifier("redisTokenServices")
    private AuthorizationServerTokenServices authorizationServerTokenServices;
    private PasswordEncoder passwordEncoder;

    /*
     * 认证成功后进行的操作
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        String header = request.getHeader("Authorization");

        log.info("当前用户是： {}", authentication.getName());

//        String password = (String) authentication.getCredentials();
        if (header == null || !header.startsWith("Basic ")) {
            // throw new UnapprovedClientAuthenticationException("请求头中无 client 信息");
            throw new InsufficientAuthenticationException("Client authentication information is missing.");
        }

        String[] tokens = extractAndDecodeHeader(header, request);
        assert tokens.length == 2;
        String clientId = tokens[0];
        String clientSecret = tokens[1];

        final String grantType = request.getParameter("grant_type");

        if (StrUtil.isEmpty(grantType)) {
            throw new InvalidRequestException("Missing grant type");
        }

        final ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientId);

        if (clientDetails == null) {
            throw new UnapprovedClientAuthenticationException(String.format("Given client ID [%s] does not match the authenticated client", clientId));
        } else if (!passwordEncoder.matches(clientSecret, clientDetails.getClientSecret())) {
            throw new UnapprovedClientAuthenticationException(String.format("Given client Secret [%s] does not match the authenticated client", clientId));
        }

        // 设置这个 Token 为自定义类型
        // TokenRequest tokenRequest = new TokenRequest(MapUtil.empty(), clientId, clientDetails.getScope(), "custom");
        TokenRequest tokenRequest = new TokenRequest(MapUtil.empty(), clientId, clientDetails.getScope(), grantType);

        final OAuth2Request oAuth2Request = tokenRequest.createOAuth2Request(clientDetails);
        final OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, authentication);
        // 这个 createAccessToken 参考 DefaultTokenServices.createAccessToken 方法
        // 这个 DefaultTokenServices 是 RedisTokenStoreConfig 文件里面创建的
        // 如果已经存在 Token 则直接从 tokenStore（RedisTokenStore） 中取得 Token
        final OAuth2AccessToken token = authorizationServerTokenServices.createAccessToken(oAuth2Authentication);

        if (Objects.isNull(token)) {
            throw new UnsupportedGrantTypeException("Unsupported grant type: " + tokenRequest.getGrantType());
        }

        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(token));
    }

    private String[] extractAndDecodeHeader(String header, HttpServletRequest request) throws IOException {
        byte[] base64Token = header.substring(6).getBytes(StandardCharsets.UTF_8);
        byte[] decoded;

        try {
            decoded = Base64.decode(base64Token);
        } catch (IllegalArgumentException e) {
            throw new BadCredentialsException("Failed to decode basic authentication token");
        }

        String token = new String(decoded, StandardCharsets.UTF_8);

        int delim = token.indexOf(":");
        if (delim == -1) {
            throw new BadCredentialsException("Invalid basic authentication token");
        }
        return new String[]{token.substring(0, delim), token.substring(delim + 1)};
    }
}
