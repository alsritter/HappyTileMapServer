package com.alsritter.service.forum.api;

import com.alsritter.common.api.CommonResult;
import com.alsritter.service.forum.common.RocketConstant;
import com.alsritter.service.forum.config.UserContext;
import com.alsritter.service.forum.mq.mqvo.MQSendCommentVo;
import com.alsritter.serviceapi.forum.domain.SendCommentTo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author alsritter
 * @version 1.0
 **/
@Slf4j
@Validated
@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentApi {
    private final RocketMQTemplate rocketMQTemplate;

    /**
     * 发送评论
     */
    @PostMapping("/send-comment")
    public CommonResult<String> sendComment(@RequestBody @Validated SendCommentTo comment) {
        MQSendCommentVo vo = new MQSendCommentVo();
        vo.setUserId(UserContext.getUser().getUserId());
        vo.setComment(comment);
        rocketMQTemplate.syncSend(RocketConstant.Topic.SEND_COMMENT, MessageBuilder.withPayload(vo).build());
        return CommonResult.success("ok");
    }
}
