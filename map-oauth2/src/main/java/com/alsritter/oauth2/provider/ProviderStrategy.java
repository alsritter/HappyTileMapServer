package com.alsritter.oauth2.provider;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

/**
 * 这里创建一个接口用来传入 HttpServletRequest
 *
 * @author alsritter
 * @version 1.0
 **/
public interface ProviderStrategy {
    Authentication authenticate(HttpServletRequest request);
}

