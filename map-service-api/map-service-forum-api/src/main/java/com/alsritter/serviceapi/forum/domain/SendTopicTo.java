package com.alsritter.serviceapi.forum.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author alsritter
 * @version 1.0
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(description = "发送主题")
public class SendTopicTo {
    @NotBlank
    @ApiModelProperty(value = "主题的标签")
    String tag;

    @NotBlank
    @Size(max = 30, min = 4, message = "最大不能超过 30, 最小不能小于 4")
    @ApiModelProperty(value = "主题的标题")
    String title;

    @NotBlank
    @Size(max = 3000, min = 4, message = "最大不能超过 3000, 最小不能小于 4")
    @ApiModelProperty(value = "主题的内容")
    String content;
}
