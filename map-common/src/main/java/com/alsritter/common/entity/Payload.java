package com.alsritter.common.entity;

import lombok.Data;

import java.util.Date;

/**
 * 为了方便后期获取 token 中的用户信息，将 token 中载荷部分单独封装成一个对象
 *
 * @author alsritter
 * @version 1.0
 **/
@Data
public class Payload<T> {
    private String id;
    private T userInfo;
    private Date expiration;
}