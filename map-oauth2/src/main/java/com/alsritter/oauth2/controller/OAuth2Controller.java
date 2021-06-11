package com.alsritter.oauth2.controller;

import com.alsritter.oauth2.api.CommonResult;
import com.alsritter.oauth2.domain.Oauth2TokenDto;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

/**
 * 自定义Oauth2获取令牌接口
 * <p>
 * 这里覆盖默认的 /oauth/token 路径，返回一个自定义的对象
 *
 * @author alsritter
 * @version 1.0
 **/
@RestController
@AllArgsConstructor
public class OAuth2Controller {

    // 自定义登陆端点参考自：
    // Spring Oauth2 - Customizing TokenEndpoint from @RequestParam to @RequestBody
    // https://stackoverflow.com/questions/50717442/spring-oauth2-customizing-tokenendpoint-from-requestparam-to-requestbody


    // private final TokenEndpoint tokenEndpoint;

    // /**
    //  * Oauth2登录认证
    //  */
    // @PostMapping("/oauth/token")
    // public ResponseEntity<CommonResult<Oauth2TokenDto>> postAccessToken(Principal principal,
    //                                                                     @RequestParam Map<String, String> parameters)
    //         throws HttpRequestMethodNotSupportedException {
    //     OAuth2AccessToken oAuth2AccessToken = tokenEndpoint.postAccessToken(principal, parameters).getBody();
    //
    //     Oauth2TokenDto oauth2TokenDto = Oauth2TokenDto.builder()
    //             .token(oAuth2AccessToken.getValue())
    //             .refreshToken(oAuth2AccessToken.getRefreshToken().getValue())
    //             .expiresIn(oAuth2AccessToken.getExpiresIn())
    //             .tokenHead("Bearer ").build();
    //
    //     return ResponseEntity.ok(CommonResult.success(oauth2TokenDto));
    // }
    //
    // private final ClientDetailsService clientDetailsService;
    // private final OAuth2RequestFactory oAuth2RequestFactory;
    //
    // @PostMapping("/oauth/login")
    // public ResponseEntity<OAuth2AccessToken> postAccessToken(final Principal principal, @RequestBody final UserLogin userLogin)
    //         throws HttpRequestMethodNotSupportedException {
    //     if (!(principal instanceof Authentication)) {
    //         throw new InsufficientAuthenticationException("Client authentication information is missing.");
    //     }
    //
    //     final Authentication client = (Authentication) principal;
    //     if (!client.isAuthenticated()) {
    //         throw new InsufficientAuthenticationException("The client is not authenticated.");
    //     }
    //     String clientId = client.getName();
    //     if (client instanceof OAuth2Authentication) {
    //         // Might be a client and user combined authentication
    //         clientId = ((OAuth2Authentication) client).getOAuth2Request().getClientId();
    //     }
    //
    //     final ClientDetails authenticatedClient = clientDetailsService.loadClientByClientId(clientId);
    //
    //
    //     final Map<String, String> parameters = new HashMap<>();
    //     parameters.put("username", userLogin.getUsername());
    //     parameters.put("password", userLogin.getPassword());
    //     parameters.put("grant_type", userLogin.getGrantType());
    //     parameters.put("refresh_token", userLogin.getRefreshToken());
    //     parameters.put("scope", userLogin.getScope());
    //
    //     final TokenRequest tokenRequest = oAuth2RequestFactory.createTokenRequest(parameters, authenticatedClient);
    //
    //     if (StringUtils.isNotBlank(clientId)) {
    //         // Only validate the client details if a client authenticated during this request.
    //         if (!clientId.equals(tokenRequest.getClientId())) {
    //             // Double check to make sure that the client ID in the token request is the same as that in the authenticated client
    //             throw new InvalidClientException(String.format("Given client ID [%s] does not match the authenticated client", clientId));
    //         }
    //     }
    //
    //     if (authenticatedClient != null) {
    //         oAuth2RequestValidator.validateScope(tokenRequest, authenticatedClient);
    //     }
    //     if (!hasText(tokenRequest.getGrantType())) {
    //         throw new InvalidRequestException("Missing grant type");
    //     }
    //
    //     final OAuth2AccessToken token;
    //
    //     if (isRefreshTokenRequest(parameters)) {
    //         // A refresh token has its own default scopes, so we should ignore any added by the factory here.
    //         tokenRequest.setScope(OAuth2Utils.parseParameterList(parameters.get(OAuth2Utils.SCOPE)));
    //         token = refreshTokenGranter.grant(tokenRequest.getGrantType().toLowerCase(), tokenRequest);
    //     } else {
    //         token = tokenGranter.grant(tokenRequest.getGrantType().toLowerCase(), tokenRequest);
    //     }
    //
    //     if (Objects.isNull(token)) {
    //         throw new UnsupportedGrantTypeException("Unsupported grant type: " + tokenRequest.getGrantType());
    //     }
    //
    //     return new ResponseEntity<>(token, HttpStatus.OK);
    // }
}