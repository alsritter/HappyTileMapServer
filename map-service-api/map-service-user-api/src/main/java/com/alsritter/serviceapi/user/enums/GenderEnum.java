package com.alsritter.serviceapi.user.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;

/**
 * 性别枚举
 */
public enum GenderEnum implements IEnum<Integer> {
    SECRECY(2, "保密"),
    MAN(1, "男"),
    WOMAN(0, "女");

    private final int value;
    private final String desc;

    GenderEnum(int value, String desc) {
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
