package com.alsritter.service.forum.mq.mqvo;

import com.alsritter.serviceapi.forum.domain.SendTopicTo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author alsritter
 * @version 1.0
 **/
@Data
@EqualsAndHashCode(callSuper = false)
public class MQSendTopicVo implements Serializable {
    private SendTopicTo topic;
    private Long userId;
}
