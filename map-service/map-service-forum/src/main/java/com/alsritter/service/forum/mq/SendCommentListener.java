package com.alsritter.service.forum.mq;

import com.alsritter.service.forum.common.RocketConstant;
import com.alsritter.service.forum.mq.mqvo.MQSendCommentVo;
import com.alsritter.service.forum.service.TbCommentService;
import com.alsritter.serviceapi.forum.enums.CommentTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQPushConsumerLifecycleListener;
import org.springframework.stereotype.Service;

/**
 * 发送评论
 *
 * @author alsritter
 * @version 1.0
 **/
@Service
@RequiredArgsConstructor
@RocketMQMessageListener(
        topic = RocketConstant.Topic.SEND_COMMENT,
        consumerGroup = RocketConstant.ConsumerGroup.SEND_COMMENT_CONSUMER)
@Slf4j
public class SendCommentListener implements RocketMQListener<MQSendCommentVo>, RocketMQPushConsumerLifecycleListener {
    private final TbCommentService commentService;

    @Override
    public void onMessage(MQSendCommentVo comment) {
        CommentTypeEnum type = comment.getComment().getType().equals(1) ?
                CommentTypeEnum.MAP : CommentTypeEnum.TOPIC;

        commentService.sendComment(
                comment.getUserId(),
                comment.getComment().getContent(),
                comment.getComment().getMasterId(),
                type);
    }

    @Override
    public void prepareStart(DefaultMQPushConsumer consumer) {
        consumer.setPullInterval(2000);
        consumer.setPullBatchSize(16);
    }
}
