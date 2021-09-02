package com.alsritter.common.page;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * 分页响应结果
 *
 * @author alsritter
 * @version 1.0
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(description = "分页查询")
public class PageVO {
    @NotNull
    @ApiModelProperty(value = "当前页")
    private Integer currentPage; // 当前页

    @ApiModelProperty(value = "显示的条数")
    private Integer pageCount = 15;  // 每页显示的条数，前端默认是10条
}
