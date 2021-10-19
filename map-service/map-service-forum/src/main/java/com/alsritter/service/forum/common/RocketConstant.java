package com.alsritter.service.forum.common;

/**
 * 消息队列的通用参数
 *
 * @author alsritter
 * @version 1.0
 **/
public final class RocketConstant {
    private RocketConstant() {}

    public static final class Topic {
        private Topic() {}
        public static final String SEND_TOPIC = "SEND_TOPIC";
        public static final String SEND_COMMENT = "SEND_COMMENT";
    }

    public static final class ConsumerGroup {
        private ConsumerGroup() {}
        public static final String SEND_TOPIC_CONSUMER = "SEND_TOPIC_CONSUMER";
        public static final String SEND_COMMENT_CONSUMER = "SEND_COMMENT_CONSUMER";
    }
}
