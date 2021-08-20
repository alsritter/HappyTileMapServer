package com.alsritter.serviceapi.user.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

public enum lockFlagEnum implements IEnum<Integer> {
    NORMAL(0, "正常"),
    LOCK(1, "锁定");

    private final int value;

    private final String desc;

    lockFlagEnum(int value, String desc) {
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
