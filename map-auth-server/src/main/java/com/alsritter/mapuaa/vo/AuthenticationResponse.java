package com.alsritter.mapuaa.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 登陆响应
 * @author alsritter
 * @version 1.0
 **/
@AllArgsConstructor
@Getter
public class AuthenticationResponse {
    private final String jwt;
}
