package com.alsritter.serviceapi.forum.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.*;

/**
 * @author alsritter
 * @version 1.0
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(description = "发送评论")
public class SendCommentTo {
    @NotNull
    @ApiModelProperty(value = "评论的对象")
    Long masterId;

    @NotBlank
    @Size(max = 3000, min = 4, message = "最大不能超过 3000, 最小不能小于 4")
    @ApiModelProperty(value = "内容")
    String content;

    @NotNull
    @ApiModelProperty(value = "评论的类型 0 是主题，1 是地图")
    Integer type;
}
