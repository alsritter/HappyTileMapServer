package com.alsritter.serviceapi.forum.enums;

/**
 * 评论类型
 */
public enum CommentTypeEnum {
    /**
     * 文章
     */
    TOPIC(0),
    /**
     * 地图
     */
    MAP(1);

    private final int value;

    CommentTypeEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
