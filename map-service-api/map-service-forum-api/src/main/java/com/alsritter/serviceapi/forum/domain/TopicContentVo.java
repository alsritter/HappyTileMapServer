package com.alsritter.serviceapi.forum.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author alsritter
 * @version 1.0
 **/
@Data
@Builder
public class TopicContentVo {
    @ApiModelProperty(value = "主题 id")
    private Long topicId;

    @ApiModelProperty(value = "回复数")
    private List<CommentVo> comments;

    @ApiModelProperty(value = "浏览量")
    private Long browsed;

    @ApiModelProperty(value = "获赞数量")
    private Long prefer;

    @ApiModelProperty(value = "文章的状态：0-正常，1-置顶，2-不可用")
    private Integer status;

    @ApiModelProperty(value = "作者")
    private String author;

    @ApiModelProperty(value = "作者 id")
    private Long authorId;

    @ApiModelProperty(value = "头像地址")
    private String authorAvatar;

    @ApiModelProperty(value = "标签")
    private String tag;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "创建时间")
    private Long createTime;

    @ApiModelProperty(value = "最后一次修改时间")
    private Long lastModifyTime;
}
