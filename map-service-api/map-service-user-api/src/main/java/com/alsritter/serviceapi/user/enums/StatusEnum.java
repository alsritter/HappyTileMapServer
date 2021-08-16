package com.alsritter.serviceapi.user.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;

public enum StatusEnum implements IEnum<Integer> {
    NORMAL(0, "正常"),
    NO_COMMENT(1, "不可以评论"),
    NO_LOGIN(2, "不可以登录");

    private final int value;
    private final String desc;

    StatusEnum(int value, String desc) {
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
