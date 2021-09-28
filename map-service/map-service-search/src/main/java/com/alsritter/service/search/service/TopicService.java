package com.alsritter.service.search.service;

import com.alsritter.service.search.model.TopicDoc;
import org.springframework.data.domain.Page;

public interface TopicService {
    /**
     * 插入帖子
     */
    void saveTopic(TopicDoc topic);

    /**
     * 全文搜索帖子
     *
     * @param keyword 关键字
     */
    Page<TopicDoc> searchTopicByFull(String keyword);
}
