package com.alsritter.serviceapi.forum.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * 列表展示的文章显示的内容
 *
 * @author alsritter
 * @version 1.0
 **/
@Data
@Builder
public class TopicItemVo {
    @ApiModelProperty(value = "主题 id")
    private Long topicId;

    @ApiModelProperty(value = "回复数")
    private Integer comments;

    @ApiModelProperty(value = "浏览量")
    private Long browsed;

    @ApiModelProperty(value = "文章的状态：0-正常，1-置顶，2-不可用")
    private Integer status;

    @ApiModelProperty(value = "头像地址")
    private String authorAvatar;

    @ApiModelProperty(value = "文章作者")
    private String author;

    @ApiModelProperty(value = "文章作者 id")
    private Long authorId;

    @ApiModelProperty(value = "标签")
    private String tag;

    @ApiModelProperty(value = "标题")
    private String title;
}
