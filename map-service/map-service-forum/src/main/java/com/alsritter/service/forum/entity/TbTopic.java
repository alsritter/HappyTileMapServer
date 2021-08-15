package com.alsritter.service.forum.entity;

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
 * 主题表（或者说是文章）(tb_topic)实体类
 *
 * @author alsritter
 * @since 2021-08-15 11:30:49
 * @description auto generator
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("tb_topic")
public class TbTopic extends Model<TbTopic> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * topicId
     */
    @TableId(type = IdType.AUTO)
	private Long topicId;
    /**
     * 主题作者
     */
    private Long userId;
    /**
     * 标签
     */
    private String tag;
    /**
     * 0-正常，1-置顶，2-不可用
     */
    private Integer status;
    /**
     * 文章标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;
    /**
     * 文章浏览量
     */
    private Long browsed;
    /**
     * 点赞数
     */
    private Long prefer;
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