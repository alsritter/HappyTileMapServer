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

    VALIDATE_FAILED(30001, "参数检验失败"),
    BIND_EXCEPTION(30002, "参数绑定错误"),
    METHOD_ARGUMENT_NOT_VALID_EXCEPTION(30003, "请求参数错误"),

    USER_EXIST_EXCEPTION(35001, "存在相同的用户"),
    PHONE_EXIST_EXCEPTION(35002, "存在相同的手机号"),
    USERNAME_PASSWORD_ERROR(35003, "账号或密码错误"),
    USER_NAME_NOT_FOUND_EXCEPTION(35004, "用户名没有找到"),


    OAUTH2_EXCEPTION(40001, "OAuth 异常"),
    UNAUTHORIZED(40002, "暂未登录或该账户的登录凭证已过期，请重新登录!"),
    INSUFFICIENT_AUTHENTICATION_EXCEPTION(40003, "登陆凭证错误"),
    ACCOUNT_DISABLED(45004, "该账户已被禁用，请联系管理员!"),
    ACCOUNT_LOCKED(45005, "该账号已被锁定，请联系管理员!"),
    ACCOUNT_EXPIRED(45006, "该账号已过期，请联系管理员!"),
    PERMISSION_DENIED(45007, "没有访问权限，请联系管理员!"),
    CREDENTIALS_EXPIRED(45008, "该账户的登录凭证已过期，请重新登录!"),



    READ_TIME_OUT_EXCEPTION(50001, "远程调用服务超时，请重新再试"),
    TO_MANY_REQUEST(50002, "请求流量过大，请稍后再试"),
    SMS_CODE_EXCEPTION(50003, "验证码获取频率太高，请稍后再试"),
    FEIGN_EXCEPTION(50004, "远距离调用错误"),

    USER_INSERT_FAILED(60001, "用户插入失败");





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
