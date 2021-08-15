package com.alsritter.service.forum.service.impl;

import com.alsritter.service.forum.mapper.TbTopicMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.alsritter.service.forum.service.TbTopicService;
import org.springframework.stereotype.Service;

/**
 * 主题表（或者说是文章）服务接口实现
 *
 * @author alsritter
 * @since 2021-08-15 11:30:49
 * @description auto generator
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class TbTopicServiceImpl implements TbTopicService {
    private final TbTopicMapper tbTopicMapper;

}