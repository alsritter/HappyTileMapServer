package com.alsritter.service.forum.mq.mqvo;

import com.alsritter.serviceapi.forum.domain.SendCommentTo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 存储 MQ 的传输对象
 *
 * @author alsritter
 * @version 1.0
 **/
@Data
@EqualsAndHashCode(callSuper = false)
public class MQSendCommentVo implements Serializable {
    private SendCommentTo comment;
    private Long userId;
}
