package com.alsritter.service.forum.controller;

import com.alsritter.service.forum.service.TbTopicService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 主题表（或者说是文章）服务控制器
 *
 * @author alsritter
 * @since 2021-08-15 11:30:49
 * @description auto generator
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/tbTopic")
public class TbTopicController {
    private final TbTopicService tbTopicService;

}