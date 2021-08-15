package com.alsritter.service.forum.controller;

import com.alsritter.service.forum.service.TbCommentTopicService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 评论中间表服务控制器
 *
 * @author alsritter
 * @since 2021-08-15 11:30:49
 * @description auto generator
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/tbCommentTopic")
public class TbCommentTopicController {
    private final TbCommentTopicService tbCommentTopicService;

}