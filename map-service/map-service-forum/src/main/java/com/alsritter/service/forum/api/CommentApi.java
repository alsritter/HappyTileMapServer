package com.alsritter.service.forum.api;

import com.alsritter.common.api.CommonResult;
import com.alsritter.service.forum.config.UserContext;
import com.alsritter.service.forum.service.TbCommentService;
import com.alsritter.serviceapi.forum.domain.SendCommentTo;
import com.alsritter.serviceapi.forum.enums.CommentTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    private final TbCommentService commentService;

    /**
     * 发送主题贴
     */
    @PostMapping("/send-comment")
    public CommonResult<Long> sendComment(@RequestBody @Validated SendCommentTo comment) {
        CommentTypeEnum type = comment.getType().equals(1) ?
                CommentTypeEnum.MAP : CommentTypeEnum.TOPIC;

        return CommonResult.success(
                commentService.sendComment(
                        UserContext.getUser().getUserId(),
                        comment.getContent(),
                        comment.getMasterId(),
                        type)
        );
    }
}
