package com.alsritter.service.search.service.impl;

import cn.hutool.json.JSONUtil;
import com.alsritter.service.search.model.TopicDoc;
import com.alsritter.service.search.service.TopicService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TopicServiceImplTest {

    @Autowired
    private TopicService topicService;


    @Test
    void test() {
        Page<TopicDoc> weather = topicService.searchTopicByFull("weather");
        System.out.println(JSONUtil.toJsonStr(weather));
    }
}