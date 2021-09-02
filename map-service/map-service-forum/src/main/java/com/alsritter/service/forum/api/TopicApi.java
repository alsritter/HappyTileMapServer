package com.alsritter.service.forum.api;

import cn.hutool.core.util.StrUtil;
import com.alsritter.common.api.CommonResult;
import com.alsritter.common.page.PageVO;
import com.alsritter.service.forum.config.UserContext;
import com.alsritter.service.forum.service.TbTopicService;
import com.alsritter.serviceapi.forum.domain.SendTopicTo;
import com.alsritter.serviceapi.forum.domain.TopicContentVo;
import com.alsritter.serviceapi.forum.domain.TopicItemVo;
import com.alsritter.serviceapi.forum.entity.TbTopic;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
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
    private final TbTopicService tbTopicService;

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

    @GetMapping("/get-by-id")
    public CommonResult<TopicContentVo> getTopicByTag(@NotNull @RequestParam Integer id) {
        return CommonResult.success(tbTopicService.getTopicById(id));
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
     * 发送主题贴
     */
    @PostMapping("/send-topic")
    public CommonResult<Long> sendTopic(@RequestBody @Validated SendTopicTo data) {
        return CommonResult.success(tbTopicService.sendTopic(data.getTag(),
                data.getTitle(), data.getContent(),
                UserContext.getUser().getUserId()));
    }
}
