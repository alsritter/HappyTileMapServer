package com.alsritter.service.forum.service.impl;

import com.alsritter.common.api.ResultCode;
import com.alsritter.common.exception.BusinessException;
import com.alsritter.service.forum.mapper.TbCommentMapper;
import com.alsritter.service.forum.service.TbCommentService;
import com.alsritter.serviceapi.forum.domain.CommentVo;
import com.alsritter.serviceapi.forum.entity.TbComment;
import com.alsritter.serviceapi.forum.enums.CommentTypeEnum;
import com.alsritter.serviceapi.user.entity.TbUser;
import com.alsritter.serviceapi.user.feign.IUserClient;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
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
 * 评论表服务接口实现
 *
 * @author alsritter
 * @description 查询评论
 * @since 2021-08-15 11:30:49
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class TbCommentServiceImpl extends ServiceImpl<TbCommentMapper, TbComment> implements TbCommentService {
    private final TbCommentMapper tbCommentMapper;
    private final IUserClient userClient;

    @Override
    public IPage<CommentVo> getCommentPageByMasterId(int currentPage, int pageSize, Long masterId, CommentTypeEnum type) {
        // 多表查询的分页参考：https://segmentfault.com/a/1190000038481191
        Page<TbComment> page = new Page<>(currentPage, pageSize);
        QueryWrapper<TbComment> wrapper = new QueryWrapper<>();
        wrapper.eq("master_id", masterId);
        wrapper.eq("flag", type.getValue());
        IPage<TbComment> comments = tbCommentMapper.getCommentPageByMasterId(page, wrapper);
        return convertToCommentVo(comments);
    }

    @Override
    public List<CommentVo> getCommentByMasterId(Long masterId, CommentTypeEnum type) {
        return tbCommentMapper.getCommentByMasterId(masterId, type.getValue()).stream().map(x -> {
            // 根据 userId 取得用户信息
            TbUser user = userClient.getUser(x.getUserId());
            return CommentVo.builder()
                    .commenter(user.getUsername())
                    .commenterId(user.getUserId())
                    .commenterAvatar(user.getAvatar())
                    .content(x.getContent())
                    .prefer(x.getPrefer())
                    .floor(x.getFloor())
                    .createTime(x.getCreateTime().getTime()).build();
        }).collect(Collectors.toList());
    }

    @Override
    public Integer getCommentCountByMasterId(Long masterId, CommentTypeEnum type) {
        return tbCommentMapper.getCommentCountByMasterId(masterId, type.getValue());
    }

    @Override
    public CommentVo getCommentById() {
        return null;
    }

    @Override
    public IPage<CommentVo> getCommentPageByUserId(int currentPage, int pageSize, Long userId) {
        return null;
    }

    @Override
    public List<CommentVo> getCommentByUserId(Long userId) {
        return null;
    }

    @Override
    @Transactional(rollbackFor = BusinessException.class,
            propagation = Propagation.REQUIRED)
    public Long sendComment(Long userId, String content, Long masterId, CommentTypeEnum type) {
        // 首先查到当前帖子最后一楼
        Integer maxFloor = tbCommentMapper.getMaxFloor(masterId, type.getValue());
        maxFloor = maxFloor == null ? 1 : maxFloor + 1;

        TbComment comment = new TbComment();
        comment.setContent(content);
        comment.setUserId(userId);
        comment.setFloor(maxFloor);
        int insert = baseMapper.insert(comment);
        if (insert < 1 || comment.getCommentId() == null) {
            throw new BusinessException(ResultCode.SEND_TOPIC_FAILED);
        }
        // 再插入到中间表
        int middleInsert = tbCommentMapper.instarMiddleTable(comment.getCommentId(), masterId, type.getValue());
        if (middleInsert < 1) {
            throw new BusinessException(ResultCode.SEND_TOPIC_FAILED);
        }

        return comment.getCommentId();
    }


    private IPage<CommentVo> convertToCommentVo(IPage<TbComment> comments) {
        List<CommentVo> collect = comments.getRecords().stream().map(x -> {
            // 根据 userId 取得用户信息
            TbUser user = userClient.getUser(x.getUserId());
            return CommentVo.builder()
                    .commenter(user.getUsername())
                    .commenterId(user.getUserId())
                    .commenterAvatar(user.getAvatar())
                    .content(x.getContent())
                    .prefer(x.getPrefer())
                    .floor(x.getFloor())
                    .createTime(x.getCreateTime().getTime()).build();
        }).collect(Collectors.toList());

        Page<CommentVo> newComData = new Page<>(comments.getCurrent(),
                comments.getSize());
        newComData.setRecords(collect);
        newComData.setTotal(comments.getTotal());
        return newComData;
    }
}