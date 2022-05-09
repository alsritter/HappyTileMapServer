package com.alsritter.service.forum.api;

import cn.hutool.core.util.StrUtil;
import com.alsritter.common.api.CommonResult;
import com.alsritter.common.page.PageVO;
import com.alsritter.service.forum.common.RocketConstant;
import com.alsritter.service.forum.config.UserContext;
import com.alsritter.service.forum.mq.mqvo.MQSendTopicVo;
import com.alsritter.service.forum.service.TbTopicService;
import com.alsritter.serviceapi.forum.domain.SendTopicTo;
import com.alsritter.serviceapi.forum.domain.TopicContentVo;
import com.alsritter.serviceapi.forum.domain.TopicItemVo;
import com.alsritter.serviceapi.forum.entity.TbTopic;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;

/**
 * 主题相关（文章）
 *
 * @author alsritter
 * @version 1.0
 **/
@Slf4j
@Validated
@RestController
@RequestMapping("/topics")
@RequiredArgsConstructor
public class TopicApi {

    private final RedisTemplate<String, Object> redisTemplate;
    private final TbTopicService tbTopicService;
    private final RocketMQTemplate rocketMQTemplate;

    /**
     * 把数据库的阅读量，和点赞数写入 Redis 里面
     */
    @PostConstruct
    public void init() {
        log.info("数据库 初始化开始");
        // 将数据库中的数据写入 Redis
        // List<Article> articleList = articleService.queryAll();
        // articleList.forEach(article -> {
        //     //将浏览量写入redis
        //     //分别有浏览量，点赞数，评论数
        //     HashMap<String, Object> h1 = new HashMap<>();
        //     h1.put("viewNum",article.getViewNum());
        //     h1.put("likeNum",article.getLikeNum());
        //     h1.put("commentNum",article.getCommentNum());
        //     redisUtil.zsAdd("commentNum",article.getArticleId().toString(),h1);
        //     redisUtil.zsAdd("viewNum",article.getArticleId().toString(),h1);
        //     redisUtil.zsAdd("likeNum",article.getArticleId().toString(),h1);
        // });

        log.info("已写入redis");

    }

    /**
     * 指定了启动延迟 60 秒，并以 60 秒的间隔执行任务去检查 Redis
     */
    @Scheduled(initialDelay = 60_000, fixedRate = 60_000)
    public void checkTopicBrowsed() {
        // log.debug("Start check system status...");
    }


    @PostMapping("/get-all")
    public CommonResult<IPage<TopicItemVo>> getAllTopic(@Validated PageVO page) {
        return CommonResult.success(tbTopicService.getAllItem(page.getCurrentPage(), page.getPageCount()));
    }

    @PostMapping("/get-by-tag")
    public CommonResult<IPage<TopicItemVo>> getTopicByTag(TbTopic topic, @Validated PageVO page) {
        IPage<TopicItemVo> result = null;
        if (topic.getTag() == null) {
            result = tbTopicService.getAllItem(page.getCurrentPage(), page.getPageCount());
        } else if ("全部".equals(topic.getTag())) {
            result = tbTopicService.getAllItem(page.getCurrentPage(), page.getPageCount());
        } else {
            result = tbTopicService.getTopicItemByTag(page.getCurrentPage(), page.getPageCount(), topic.getTag());
        }
        return CommonResult.success(result);
    }


    @PostMapping("/search")
    public CommonResult<IPage<TopicItemVo>> searchTopics(String str, @Validated PageVO page) {
        if (StrUtil.isEmpty(str)) {
            return getAllTopic(page);
        }
        return CommonResult.success(tbTopicService.searchTopics(page.getCurrentPage(), page.getPageCount(), str));
    }

    /**
     * 注意。这里必须使用 Post 不能用 Get，只有 Post 才能这样取多个对象
     */
    @PostMapping("/test")
    public CommonResult<String> test(TbTopic topic, @Validated PageVO page) {
        return CommonResult.success(topic.getTopicId().toString() + page.getCurrentPage());
    }


    /**
     * 根据 ID 取得 Topic，同时计数（增加阅览量）
     * 技术方案的缺陷：
     * 需要频繁的修改 Redis,耗费 CPU，高并发修改 redis 会导致 redisCPU 100%
     */
    @GetMapping("/get-by-id")
    public CommonResult<TopicContentVo> getTopicByTag(@NotNull @RequestParam Integer id) {
        //redis key
        String key = "article:" + id;
        //调用redis的increment计数器命令
        Long n = redisTemplate.opsForValue().increment(key);
        log.info("key={},阅读量为{}", key, n);
        return CommonResult.success(tbTopicService.getTopicById(id));
    }

    /**
     * 发送主题贴
     */
    @PostMapping("/send-topic")
    public CommonResult<String> sendTopic(@RequestBody @Validated SendTopicTo data) {
        MQSendTopicVo vo = new MQSendTopicVo();
        vo.setUserId(UserContext.getUser().getUserId());
        vo.setTopic(data);
        rocketMQTemplate.syncSend(RocketConstant.Topic.SEND_TOPIC, MessageBuilder.withPayload(vo).build());
        return CommonResult.success("ok");
    }

}
