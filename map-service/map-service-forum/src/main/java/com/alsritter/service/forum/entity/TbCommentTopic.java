package com.alsritter.service.forum.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 评论中间表(tb_comment_topic)实体类
 *
 * @author alsritter
 * @since 2021-08-15 11:30:49
 * @description auto generator
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("tb_comment_topic")
public class TbCommentTopic extends Model<TbCommentTopic> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
	private Long id;
    /**
     * map 表或 topic 表的 id
     */
    private Long masterId;
    /**
     * 0-topic，1-map
     */
    private Integer flag;
    /**
     * 0-正常，1-删除
     */
    private Integer delFlag;

}