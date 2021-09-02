package com.alsritter.gateway.component;

import cn.hutool.json.JSONUtil;
import com.alsritter.common.api.ResultCode;
import com.alsritter.common.exception.BusinessException;
import com.nimbusds.oauth2.sdk.ParseException;
import com.nimbusds.oauth2.sdk.TokenIntrospectionResponse;
import com.nimbusds.oauth2.sdk.TokenIntrospectionSuccessResponse;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import com.nimbusds.oauth2.sdk.id.Audience;
import com.nimbusds.oauth2.sdk.util.JSONObjectUtils;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionException;
import org.springframework.security.oauth2.server.resource.introspection.ReactiveOpaqueTokenIntrospector;
import org.springframework.util.Assert;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URL;
import java.time.Instant;
import java.util.*;

import static org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionClaimNames.*;

/**
 * 参考默认的检查工具类
 * {@link org.springframework.security.oauth2.server.resource.introspection.NimbusReactiveOpaqueTokenIntrospector}
 * <p>
 * 检查 CheckTokenEndpoint 工具类
 *
 * @author alsritter
 * @version 1.0
 **/
@Slf4j
public class CustomNimbusReactiveOpaqueTokenIntrospector implements ReactiveOpaqueTokenIntrospector {

    private final URI introspectionUri;
    private final WebClient.Builder webClient;

    public CustomNimbusReactiveOpaqueTokenIntrospector(String introspectionUri, WebClient.Builder webClient) {
        Assert.hasText(introspectionUri, "introspectionUri cannot be empty");
        this.webClient = webClient;
        this.introspectionUri = URI.create(introspectionUri);
    }

    @Override
    public Mono<OAuth2AuthenticatedPrincipal> introspect(String token) {
        return Mono.just(token)
                .flatMap(this::makeRequest)
                .flatMap(this::adaptToNimbusResponse)
                .map(this::parseNimbusResponse)
                .map(this::castToNimbusSuccess)
                .doOnNext(response -> validate(token, response))
                .map(this::convertClaimsSet);
        // .onErrorMap(e -> !(e instanceof OAuth2IntrospectionException || e instanceof BusinessException), this::onError);
    }

    /**
     * 根据 Token 发请求校验（oauth/check_token）
     */
    private Mono<ClientResponse> makeRequest(String token) {
        return this.webClient.build().post()
                .uri(this.introspectionUri)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .body(BodyInserters.fromFormData("token", token))
                .exchange();
    }

    /**
     * 响应数据，通过 flatMap 传递进来，map 返回的是原来的序列，flatMap 返回的是一个新的序列
     */
    private Mono<HTTPResponse> adaptToNimbusResponse(ClientResponse responseEntity) {
        // 这里是从 CheckTokenEndpoint 返回的结果
        HTTPResponse response = new HTTPResponse(responseEntity.rawStatusCode());
        response.setHeader(HttpHeaders.CONTENT_TYPE, responseEntity.headers().contentType().get().toString());
        if (response.getStatusCode() != HTTPResponse.SC_OK) {
            // Reactor 使用参考：https://stackoverflow.com/questions/53595420/correct-way-of-throwing-exceptions-with-reactor
            return responseEntity
                    .bodyToMono(String.class)
                    .doOnNext(System.out::println)
                    .flatMap(json -> {
                        cn.hutool.json.JSONObject jsonObject = JSONUtil.parseObj(json);
                        Integer code = jsonObject.getInt("code");
                        String message = jsonObject.getStr("message");
                        if (code != null && message != null) {
                            if (code == ResultCode.ACCOUNT_EXPIRED.getCode()) {
                                return Mono.error(new BusinessException(ResultCode.ACCOUNT_EXPIRED));
                            }
                            return Mono.error(new BusinessException(code, message));
                        } else {
                            return Mono.error(new BusinessException(ResultCode.ACCOUNT_INTROSPECTION_EXCEPTION));
                        }
                    });
        }

        return responseEntity.bodyToMono(String.class)
                .doOnNext(response::setContent)
                .map(body -> response);
    }

    private TokenIntrospectionResponse parseNimbusResponse(HTTPResponse response) {
        try {
            return TokenIntrospectionResponse.parse(response);
        } catch (Exception ex) {
            // throw new OAuth2IntrospectionException(ex.getMessage(), ex);
            throw new BusinessException(ResultCode.ACCOUNT_INTROSPECTION_EXCEPTION);
        }
    }

    private TokenIntrospectionSuccessResponse castToNimbusSuccess(TokenIntrospectionResponse introspectionResponse) {
        if (!introspectionResponse.indicatesSuccess()) {
            // throw new OAuth2IntrospectionException("Token introspection failed");
            throw new BusinessException(ResultCode.ACCOUNT_VALIDATE_EXCEPTION);
        }
        return (TokenIntrospectionSuccessResponse) introspectionResponse;
    }

    private void validate(String token, TokenIntrospectionSuccessResponse response) {
        // relying solely on the authorization server to validate this token (not checking 'exp', for example)
        if (!response.isActive()) {
            throw new BusinessException(ResultCode.ACCOUNT_VALIDATE_EXCEPTION);
        }
    }

    private OAuth2AuthenticatedPrincipal convertClaimsSet(TokenIntrospectionSuccessResponse response) {
        Map<String, Object> claims = response.toJSONObject();
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        List<String> audiences = new ArrayList<>();
        JSONObject parameters = response.getParameters();

        if (!claims.containsKey("user_name")) {
            List<String> scopes = null;
            try {
                scopes = JSONObjectUtils.getStringList(parameters, "scope");
            } catch (ParseException e) {
                scopes = Collections.emptyList();
            }

            if (!scopes.isEmpty()) {
                for (String scope : scopes) {
                    String authorityPrefix = "SCOPE_";
                    authorities.add(new SimpleGrantedAuthority(authorityPrefix + scope.toUpperCase(Locale.ROOT)));
                }
                claims.put(SCOPE, scopes);
            }
        } else {
            List<Audience> pasAudiences = null;
            try {
                pasAudiences = Audience.create(JSONObjectUtils.getStringList(parameters, "authorities"));
            } catch (ParseException e) {
                pasAudiences = Collections.emptyList();
            }

            if (!pasAudiences.isEmpty()) {
                for (Audience audience : pasAudiences) {
                    authorities.add(new SimpleGrantedAuthority(audience.getValue()));
                    audiences.add(audience.getValue());
                }

                claims.put(AUDIENCE, Collections.unmodifiableList(audiences));
            }
        }
        if (response.getClientID() != null) {
            claims.put(CLIENT_ID, response.getClientID().getValue());
        }
        if (response.getExpirationTime() != null) {
            Instant exp = response.getExpirationTime().toInstant();
            claims.put(EXPIRES_AT, exp);
        }
        if (response.getIssueTime() != null) {
            Instant iat = response.getIssueTime().toInstant();
            claims.put(ISSUED_AT, iat);
        }
        if (response.getIssuer() != null) {
            claims.put(ISSUER, issuer(response.getIssuer().getValue()));
        }
        if (response.getNotBeforeTime() != null) {
            claims.put(NOT_BEFORE, response.getNotBeforeTime().toInstant());
        }


        return new DefaultOAuth2AuthenticatedPrincipal(claims, authorities);
    }

    private URL issuer(String uri) {
        try {
            return new URL(uri);
        } catch (Exception ex) {
            throw new OAuth2IntrospectionException("Invalid " + ISSUER + " value: " + uri);
        }
    }

    // private BusinessException onError(Throwable e) {
    //     return new BusinessException(e.getMessage(), e);
    // }
}
