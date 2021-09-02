package com.alsritter.service.forum.service;


import com.alsritter.serviceapi.forum.domain.CommentVo;
import com.alsritter.serviceapi.forum.enums.CommentTypeEnum;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * 评论表服务接口
 *
 * @author alsritter
 * @description auto generator
 * @since 2021-08-15 11:30:49
 */
public interface TbCommentService {
    IPage<CommentVo> getCommentPageByMasterId(int currentPage, int pageSize, Long masterId, CommentTypeEnum type);

    List<CommentVo> getCommentByMasterId(Long masterId, CommentTypeEnum type);

    Integer getCommentCountByMasterId(Long masterId, CommentTypeEnum type);

    CommentVo getCommentById();

    IPage<CommentVo> getCommentPageByUserId(int currentPage, int pageSize, Long userId);

    List<CommentVo> getCommentByUserId(Long userId);

    /**
     * 发送评论，注意，要先查询当前帖子最后一楼是什么再加一插入
     */
    Long sendComment(Long userId, String content, Long masterId, CommentTypeEnum type);
}
