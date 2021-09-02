package com.alsritter.serviceapi.forum.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * 评论
 *
 * @author alsritter
 * @version 1.0
 **/
@Data
@Builder
public class CommentVo {
    @ApiModelProperty(value = "评论者")
    private String commenter;

    @ApiModelProperty(value = "评论者 id")
    private Long commenterId;

    @ApiModelProperty(value = "头像地址")
    private String commenterAvatar;

    @ApiModelProperty(value = "获赞数量")
    private Long prefer;

    @ApiModelProperty(value = "评论所在楼")
    private Integer floor;

    @ApiModelProperty(value = "评论内容")
    private String content;

    @ApiModelProperty(value = "评论时间")
    private Long createTime;
}
