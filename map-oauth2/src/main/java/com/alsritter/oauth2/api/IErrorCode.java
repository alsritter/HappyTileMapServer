package com.alsritter.oauth2.api;

/**
 * 封装 API 的错误码
 */
public interface IErrorCode {
    long getCode();
    String getMessage();
}
