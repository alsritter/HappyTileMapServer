package com.alsritter.serviceapi.forum.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 评论表(tb_comment)实体类
 *
 * @author alsritter
 * @since 2021-08-15 11:30:49
 * @description auto generator
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("tb_comment")
public class TbComment extends Model<TbComment> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 评论id
     */
    @TableId(type = IdType.AUTO)
	private Long commentId;
    /**
     * 评论所在楼
     */
    private Integer floor;
    /**
     * 评论的人
     */
    private Long userId;
    /**
     * 内容
     */
    private String content;
    /**
     * createTime
     */
    private Date createTime;
    /**
     * lastModifyTime
     */
    private Date lastModifyTime;
    /**
     * 0-正常，1-删除
     */
    private Integer delFlag;

}