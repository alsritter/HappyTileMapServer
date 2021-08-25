package com.alsritter.gateway.component;

import com.nimbusds.oauth2.sdk.ParseException;
import com.nimbusds.oauth2.sdk.TokenIntrospectionResponse;
import com.nimbusds.oauth2.sdk.TokenIntrospectionSuccessResponse;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import com.nimbusds.oauth2.sdk.id.Audience;
import com.nimbusds.oauth2.sdk.util.JSONObjectUtils;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.BadOpaqueTokenException;
import org.springframework.security.oauth2.server.resource.introspection.NimbusReactiveOpaqueTokenIntrospector;
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
import static org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionClaimNames.ISSUER;

/**
 * 参考默认的检查工具类
 * NimbusReactiveOpaqueTokenIntrospector
 *
 * 检查 CheckTokenEndpoint 工具类
 *
 * @author alsritter
 * @version 1.0
 **/
@Slf4j
public class MyNimbusReactiveOpaqueTokenIntrospector implements ReactiveOpaqueTokenIntrospector {
    private final URI introspectionUri;
    private final WebClient webClient;

    public MyNimbusReactiveOpaqueTokenIntrospector(String introspectionUri, String clientId, String clientSecret) {
        Assert.hasText(introspectionUri, "introspectionUri cannot be empty");
        Assert.hasText(clientId, "clientId cannot be empty");
        Assert.notNull(clientSecret, "clientSecret cannot be null");

        this.introspectionUri = URI.create(introspectionUri);
        this.webClient = WebClient.builder()
                .defaultHeaders(h -> h.setBasicAuth(clientId, clientSecret))
                .build();
    }

    public MyNimbusReactiveOpaqueTokenIntrospector(String introspectionUri, WebClient webClient) {
        Assert.hasText(introspectionUri, "introspectionUri cannot be null");
        Assert.notNull(webClient, "webClient cannot be null");

        this.introspectionUri = URI.create(introspectionUri);
        this.webClient = webClient;
    }

    @Override
    public Mono<OAuth2AuthenticatedPrincipal> introspect(String token) {
        return Mono.just(token)
                .flatMap(this::makeRequest)
                .flatMap(this::adaptToNimbusResponse)
                .map(this::parseNimbusResponse)
                .map(this::castToNimbusSuccess)
                .doOnNext(response -> validate(token, response))
                .map(this::convertClaimsSet)
                .onErrorMap(e -> !(e instanceof OAuth2IntrospectionException), this::onError);
    }

    private Mono<ClientResponse> makeRequest(String token) {
        return this.webClient.post()
                .uri(this.introspectionUri)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_UTF8_VALUE)
                .body(BodyInserters.fromFormData("token", token))
                .exchange();
    }

    private Mono<HTTPResponse> adaptToNimbusResponse(ClientResponse responseEntity) {
        HTTPResponse response = new HTTPResponse(responseEntity.rawStatusCode());
        response.setHeader(HttpHeaders.CONTENT_TYPE, responseEntity.headers().contentType().get().toString());
        if (response.getStatusCode() != HTTPResponse.SC_OK) {
            return responseEntity.bodyToFlux(DataBuffer.class)
                    .map(DataBufferUtils::release)
                    .then(Mono.error(new OAuth2IntrospectionException(
                            "Introspection endpoint responded with " + response.getStatusCode())));
        }
        return responseEntity.bodyToMono(String.class)
                .doOnNext(response::setContent)
                .map(body -> response);
    }

    private TokenIntrospectionResponse parseNimbusResponse(HTTPResponse response) {
        try {
            return TokenIntrospectionResponse.parse(response);
        } catch (Exception ex) {
            throw new OAuth2IntrospectionException(ex.getMessage(), ex);
        }
    }

    private TokenIntrospectionSuccessResponse castToNimbusSuccess(TokenIntrospectionResponse introspectionResponse) {
        if (!introspectionResponse.indicatesSuccess()) {
            throw new OAuth2IntrospectionException("Token introspection failed");
        }
        return (TokenIntrospectionSuccessResponse) introspectionResponse;
    }

    private void validate(String token, TokenIntrospectionSuccessResponse response) {
        // relying solely on the authorization server to validate this token (not checking 'exp', for example)
        if (!response.isActive()) {
            throw new BadOpaqueTokenException("Provided token isn't active");
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
                pasAudiences = Audience.create(JSONObjectUtils.getStringList(parameters , "authorities"));
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

    private OAuth2IntrospectionException onError(Throwable e) {
        return new OAuth2IntrospectionException(e.getMessage(), e);
    }
}
