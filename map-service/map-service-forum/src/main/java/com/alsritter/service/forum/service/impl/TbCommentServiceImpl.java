package com.alsritter.service.forum.service.impl;

import com.alsritter.service.forum.mapper.TbCommentMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.alsritter.service.forum.service.TbCommentService;
import org.springframework.stereotype.Service;

/**
 * 评论表服务接口实现
 *
 * @author alsritter
 * @since 2021-08-15 11:30:49
 * @description auto generator
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class TbCommentServiceImpl implements TbCommentService {
    private final TbCommentMapper tbCommentMapper;

}