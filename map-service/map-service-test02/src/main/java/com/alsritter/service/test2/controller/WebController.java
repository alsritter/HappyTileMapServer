package com.alsritter.service.test2.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author alsritter
 * @version 1.0
 **/
@RestController
public class WebController {

    @PreAuthorize("hasAnyAuthority('DELETE','QUERY')")
    @GetMapping("/test")
    public String securedResource(Authentication auth) {
        return "This is a SECURED resource. Authentication: " + auth.getName() + "; Authorities: " + auth.getAuthorities();
    }

    @GetMapping("/unsecured")
    public String unsecuredResource() {
        return "This is an unsecured resource";
    }
}

