package com.alsritter.common.api;

import cn.hutool.http.HttpStatus;

/**
 * 枚举了一些常用API操作码
 */
public enum ResultCode implements IErrorCode {

    SUCCESS(HttpStatus.HTTP_OK, "操作成功"),
    LOGIN_SUCCESS(HttpStatus.HTTP_OK, "登录成功"),
    FAILED(HttpStatus.HTTP_INTERNAL_ERROR, "操作失败"),

    UNKNOW_EXCEPTION(HttpStatus.HTTP_INTERNAL_ERROR, "系统未知异常"),

    TO_MANY_REQUEST(HttpStatus.HTTP_FORBIDDEN, "请求流量过大，请稍后再试"),
    SMS_CODE_EXCEPTION(HttpStatus.HTTP_FORBIDDEN, "验证码获取频率太高，请稍后再试"),

    READ_TIME_OUT_EXCEPTION(HttpStatus.HTTP_GATEWAY_TIMEOUT, "远程调用服务超时，请重新再试"),

    USER_EXIST_EXCEPTION(15001, "存在相同的用户"),
    PHONE_EXIST_EXCEPTION(15002, "存在相同的手机号"),

    USERNAME_PASSWORD_ERROR(15003, "账号或密码错误"),

    VALIDATE_FAILED(HttpStatus.HTTP_BAD_REQUEST, "参数检验失败"),
    UNAUTHORIZED(HttpStatus.HTTP_UNAUTHORIZED, "暂未登录或该账户的登录凭证已过期，请重新登录!"),

    ACCOUNT_DISABLED(15004, "该账户已被禁用，请联系管理员!"),
    ACCOUNT_LOCKED(15005, "该账号已被锁定，请联系管理员!"),
    ACCOUNT_EXPIRED(15006, "该账号已过期，请联系管理员!"),
    PERMISSION_DENIED(15007, "没有访问权限，请联系管理员!"),
    CREDENTIALS_EXPIRED(15008, "该账户的登录凭证已过期，请重新登录!"),

    USER_INSERT_FAILED(16001, "用户插入失败");


    private final int code;
    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
