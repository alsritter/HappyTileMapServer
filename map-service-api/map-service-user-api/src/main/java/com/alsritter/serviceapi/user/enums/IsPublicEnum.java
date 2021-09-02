package com.alsritter.serviceapi.user.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;

public enum IsPublicEnum implements IEnum<Integer> {
    PRIVATE(0, "需要验证的资源"),
    PUBLIC(1, "开放资源");

    private final int value;

    private final String desc;

    IsPublicEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return this.desc;
    }
}
