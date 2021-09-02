package com.alsritter.service.forum.service;


import com.alsritter.serviceapi.forum.domain.TopicContentVo;
import com.alsritter.serviceapi.forum.domain.TopicItemVo;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * 主题表（或者说是文章）服务接口
 *
 * @author alsritter
 * @description auto generator
 * @since 2021-08-15 11:30:49
 */
public interface TbTopicService {
    IPage<TopicItemVo> getAllItem(int currentPage, int pageSize);

    /**
     * 取得主题概要（文章部分只显示 100字）
     */
    IPage<TopicItemVo> getTopicItemByTag(int currentPage, int pageSize, String tag);

    /**
     * 取得文章
     */
    TopicContentVo getTopicById(int topicId);

    /**
     * 根据关键字全文搜索
     */
    IPage<TopicItemVo> searchTopics(int currentPage, int pageSize, String str);

    /**
     * 发布文章
     * @return 文章的 id
     */
    Long sendTopic(String tag, String title, String content, Long userId);
}
