package com.alsritter.service.search;

import cn.hutool.core.util.RandomUtil;
import com.alsritter.service.search.model.TopicDoc;
import com.alsritter.service.search.repository.ReactiveTopicDocRepository;
import com.alsritter.service.search.repository.TopicDocRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import reactor.core.publisher.Flux;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SearchApplicationTest {
    @Autowired
    private TopicDocRepository topicDocRepository;
    @Autowired
    private ReactiveTopicDocRepository reactiveTopicDocRepository;

    // 清空数据并验证数据条数是否已为0
    @Order(0)
    @Test
    @DisplayName("清空所有数据")
    void clearIndex() {
        topicDocRepository.deleteAll();
        assertTrue(0 == topicDocRepository.count(), "数据已清空完毕");
    }

    /**
     * 这个 @CsvSource 是参数化测试，一般用来测试多参数的数据
     * 具体参考：https://doczhcn.gitbook.io/junit5/index/index-2/parameterized-tests
     * 简单阅读：JUnit5学习之六：参数化测试(Parameterized Tests)基础 https://www.cnblogs.com/bolingcavalry/p/14454696.html
     */
    @ParameterizedTest
    @Order(1)
    @CsvSource({
            "How's the weather today?, issue, Today I came back to my hometown. It was a sunny and windy day.",
            "How many people are there now?, issue, There should be tens of thousands of people inside.",
    })
    @DisplayName("初始化数据")
    void initTest(String title, String tag, String content) {
        // 每插入一条数据记录数加一
        // String uuid = UUID.randomUUID().toString();
        Long id = RandomUtil.randomLong(10000);
        Instant time = Instant.now();
        TopicDoc topic = new TopicDoc(Long.valueOf(id), tag, title, content, time);
        // 保存一条新的 doc 并获取返回数据
        TopicDoc doc = topicDocRepository.save(topic);
        // 对比存入后返回的数据与传入参数的 title 是否一致
        assertEquals(topic.getTitle(), doc.getTitle(), "Title 一致");
    }


    @DisplayName("获取消息条数")
    @Order(2)
    @Test
    void getTopicQuantity() {
        // 通过响应式方式获取所有消息
        Flux<TopicDoc> msg = reactiveTopicDocRepository.findAll();
        // 通过一般方式获取消息条数
        long msgCount = topicDocRepository.count();
        System.out.println("共有消息，msg.count() ： " + msg.count().block().longValue());
        System.out.println("共有消息，msgCount ： " + msgCount);
        // 如果数据量特别多这个断言是会失败的，因为findAll如果不加分页，则其会默认最多1000条
        assertTrue(
                (msg.count().block().longValue() == msgCount), "消息条数符合预期");
    }


    private static Long tstId = 1234567L;


    @DisplayName("保存新的主题")
    @Order(3)
    @Test
    void saveTopic() {
        TopicDoc topic =
                new TopicDoc(
                        tstId,
                        "work",
                        "沁园春·辛丑春重返达坂城",
                        "放眼乾坤，漫步山川，回首汉唐。想旌旗西指，乌孙路远；管弦东去，赤子情长。营帐生烟，轮蹄迸火，于此都曾系马缰。登临处，问悠悠岁月，几度沧桑。  且随雁阵翱翔，把城外城中细打量。对蓝天白日，车飞高速；青杨红柳，人在仙乡。雪浪清心，冰峰爽目，铺绿良田扩四方。吾来也，用吟诗摄影，留住春光。",
                        Instant.now());
        TopicDoc doc = topicDocRepository.save(topic);
        assertAll(
                "msg",
                () -> assertEquals(topic.getTitle(), doc.getTitle()),
                () -> assertEquals(topic.getTopicId(), doc.getTopicId()));
    }


    @DisplayName("更新主题")
    @Order(4)
    @Test
    void updateTopic() {
        Optional<TopicDoc> optionalTopicDoc = topicDocRepository.findById(tstId);

        TopicDoc doc = optionalTopicDoc.get();
        String title = doc.getTitle();
        String newTitle = "沁园春·修改后的标题";
        doc.setTitle(newTitle);
        TopicDoc savedMsg = topicDocRepository.save(doc);
        assertAll(
                "msg",
                () -> assertEquals(savedMsg.getTitle(), newTitle),
                () -> assertNotEquals(savedMsg.getTitle(), title),
                () -> assertEquals(savedMsg.getTopicId(), tstId));
    }


    @DisplayName("删除主题")
    @Order(5)
    @Test
    void delTopic() {
        // 看看该消息是否存在
        boolean existTopicBeforeDel = topicDocRepository.existsById(tstId);
        // 删除该消息
        topicDocRepository.deleteById(tstId);
        // 看看该消息是否还存在
        boolean existTopicAfterDel = topicDocRepository.existsById(tstId);
        assertAll("exist topic", () -> assertTrue(existTopicBeforeDel), () -> assertFalse(existTopicAfterDel));
    }


    @DisplayName("获取运营发的消息")
    @Order(7)
    @Test
    void getTopicDocFromTag() {
        String tag = "issue";
        Flux<TopicDoc> topics = reactiveTopicDocRepository
                .findByTag(tag, PageRequest.of(0, 2, Sort.by("topicId").descending()));
        System.out.println("消息共有: " + topics.count().block().longValue());
        topics.toStream()
                .forEach(
                        topic -> {
                            System.out.println("内容是: " + topic.getContent());
                            System.out.println("标题是: " + topic.getTitle());
                        });
    }
}