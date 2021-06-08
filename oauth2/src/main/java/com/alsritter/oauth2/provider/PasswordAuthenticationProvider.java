package com.alsritter.oauth2.provider;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.http.HttpServletRequest;

/**
 * @author alsritter
 * @version 1.0
 **/
public class PasswordAuthenticationProvider implements AuthenticationProvider,ProviderStrategy{
    @Override
    public Authentication authenticate(HttpServletRequest request) {
        return null;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return false;
    }
}
