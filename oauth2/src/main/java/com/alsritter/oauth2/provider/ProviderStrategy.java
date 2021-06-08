package com.alsritter.oauth2.provider;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

/**
 * @author alsritter
 * @version 1.0
 **/
public interface ProviderStrategy {
    Authentication authenticate(HttpServletRequest request);
}

