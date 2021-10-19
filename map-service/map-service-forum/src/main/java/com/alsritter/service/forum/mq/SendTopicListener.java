package com.alsritter.service.forum.mq;

import com.alsritter.service.forum.common.RocketConstant;
import com.alsritter.service.forum.config.UserContext;
import com.alsritter.service.forum.mq.mqvo.MQSendTopicVo;
import com.alsritter.service.forum.service.TbTopicService;
import com.alsritter.serviceapi.forum.domain.SendTopicTo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQPushConsumerLifecycleListener;
import org.springframework.stereotype.Service;

/**
 * 发送帖子
 *
 * @author alsritter
 * @version 1.0
 **/
@Service
@RequiredArgsConstructor
@RocketMQMessageListener(
        topic = RocketConstant.Topic.SEND_TOPIC,
        consumerGroup = RocketConstant.ConsumerGroup.SEND_TOPIC_CONSUMER)
@Slf4j
public class SendTopicListener implements RocketMQListener<MQSendTopicVo>, RocketMQPushConsumerLifecycleListener {
    private final TbTopicService tbTopicService;

    @Override
    public void onMessage(MQSendTopicVo data) {
        // praiseRecordService.insert(vo.copyProperties(SendTopicTo::new));
        tbTopicService.sendTopic(data.getTopic().getTag(),
                data.getTopic().getTitle(), data.getTopic().getContent(),
                data.getUserId());
    }

    @Override
    public void prepareStart(DefaultMQPushConsumer consumer) {
        // 每次拉取的间隔，单位为毫秒
        consumer.setPullInterval(2000);
        // 设置每次从队列中拉取的消息数为 16
        consumer.setPullBatchSize(16);
    }
}
