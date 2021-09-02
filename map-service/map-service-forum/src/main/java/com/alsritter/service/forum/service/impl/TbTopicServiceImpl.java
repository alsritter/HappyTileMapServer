package com.alsritter.service.forum.service.impl;

import com.alsritter.common.api.ResultCode;
import com.alsritter.common.exception.BusinessException;
import com.alsritter.service.forum.mapper.TbTopicMapper;
import com.alsritter.service.forum.service.TbCommentService;
import com.alsritter.service.forum.service.TbTopicService;
import com.alsritter.serviceapi.forum.domain.TopicContentVo;
import com.alsritter.serviceapi.forum.domain.TopicItemVo;
import com.alsritter.serviceapi.forum.entity.TbTopic;
import com.alsritter.serviceapi.forum.enums.CommentTypeEnum;
import com.alsritter.serviceapi.user.entity.TbUser;
import com.alsritter.serviceapi.user.feign.IUserClient;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 主题表（或者说是文章）服务接口实现
 *
 * @author alsritter
 * @description auto generator
 * @since 2021-08-15 11:30:49
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class TbTopicServiceImpl extends ServiceImpl<TbTopicMapper, TbTopic> implements TbTopicService {
    private final TbTopicMapper tbTopicMapper;
    private final IUserClient userClient;
    private final TbCommentService commentService;

    // TODO: 这里维护一个缓存队列（后台线程维护队列长度）
    // TODO: 点赞模块的设计，访问量功能单独优化
    @Override
    public IPage<TopicItemVo> getAllItem(int currentPage, int pageSize) {
        Page<TbTopic> page = new Page<>(currentPage, pageSize);
        IPage<TbTopic> topicIPage = baseMapper.selectPage(page,
                Wrappers.<TbTopic>query().lambda()
                        .ne(TbTopic::getStatus, 2)
                        .orderByDesc(TbTopic::getStatus));
        return convertToTopicItemVo(topicIPage);
    }

    @Override
    public IPage<TopicItemVo> getTopicItemByTag(int currentPage, int pageSize, String tag) {
        Page<TbTopic> page = new Page<>(currentPage, pageSize);
        IPage<TbTopic> topicIPage = baseMapper.selectPage(page,
                Wrappers.<TbTopic>query().lambda()
                        .eq(TbTopic::getTag, tag)
                        .ne(TbTopic::getStatus, 2)
                        .orderByDesc(TbTopic::getStatus));
        return convertToTopicItemVo(topicIPage);
    }

    @Override
    public TopicContentVo getTopicById(int topicId) {
        TbTopic tbTopic = baseMapper.selectOne(Wrappers.<TbTopic>query().lambda()
                .eq(TbTopic::getTopicId, topicId));
        // 取得评论


        TbUser user = userClient.getUser(tbTopic.getUserId());

        return TopicContentVo.builder()
                .topicId(tbTopic.getTopicId())
                .status(tbTopic.getStatus())
                .browsed(tbTopic.getBrowsed())
                .tag(tbTopic.getTag())
                .title(tbTopic.getTitle())
                .comments(commentService.getCommentByMasterId(tbTopic.getTopicId(), CommentTypeEnum.TOPIC))
                .author(user.getUsername())
                .authorId(user.getUserId())
                .content(tbTopic.getContent())
                .createTime(tbTopic.getCreateTime().getTime())
                .lastModifyTime(tbTopic.getLastModifyTime().getTime())
                .authorAvatar(user.getAvatar())
                .prefer(tbTopic.getPrefer())
                .build();
    }

    /**
     * 全文搜索 TODO: 使用 ES 重写这部分
     */
    @Override
    public IPage<TopicItemVo> searchTopics(int currentPage, int pageSize, String str) {
        Page<TbTopic> page = new Page<>(currentPage, pageSize);
        IPage<TbTopic> topicIPage = baseMapper.selectPage(page,
                Wrappers.<TbTopic>query().lambda()
                        .like(TbTopic::getContent, str)
                        .like(TbTopic::getTitle, str)
                        .ne(TbTopic::getStatus, 2)
                        .orderByDesc(TbTopic::getStatus)
        );
        return convertToTopicItemVo(topicIPage);
    }

    /**
     * 返回创建文章的 id
     */
    @Override
    @Transactional(rollbackFor = BusinessException.class,
            propagation = Propagation.REQUIRED)
    public Long sendTopic(String tag, String title, String content, Long userId) {
        TbTopic topic = new TbTopic();
        topic.setContent(content);
        topic.setUserId(userId);
        topic.setTag(tag);
        topic.setTitle(title);
        int insert = baseMapper.insert(topic);
        if (insert < 1 || topic.getTopicId() == null) {
            throw new BusinessException(ResultCode.SEND_TOPIC_FAILED);
        }

        return topic.getTopicId();
    }


    private IPage<TopicItemVo> convertToTopicItemVo(IPage<TbTopic> topicIPage) {

        List<TopicItemVo> newData =
                topicIPage.getRecords().stream()
                        .map(x -> {
                            TbUser user = userClient.getUser(x.getUserId());
                            return TopicItemVo.builder()
                                    .topicId(x.getTopicId())
                                    .status(x.getStatus())
                                    .author(user.getUsername())
                                    .authorId(user.getUserId())
                                    .authorAvatar(user.getAvatar())
                                    .browsed(x.getBrowsed())
                                    .tag(x.getTag())
                                    .title(x.getTitle())
                                    .comments(commentService.getCommentCountByMasterId(x.getTopicId(), CommentTypeEnum.TOPIC))
                                    .build();
                        }).collect(Collectors.toList());

        Page<TopicItemVo> topicItemVoPage = new Page<>(topicIPage.getCurrent(),
                topicIPage.getSize());
        topicItemVoPage.setRecords(newData);
        topicItemVoPage.setTotal(topicIPage.getTotal());
        return topicItemVoPage;
    }
}