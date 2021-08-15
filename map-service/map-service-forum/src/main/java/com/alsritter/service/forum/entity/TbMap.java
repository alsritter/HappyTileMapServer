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
 * 地图表(tb_map)实体类
 *
 * @author alsritter
 * @since 2021-08-15 11:30:49
 * @description auto generator
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("tb_map")
public class TbMap extends Model<TbMap> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * mapId
     */
    @TableId(type = IdType.AUTO)
	private Long mapId;
    /**
     * 地图作者
     */
    private Long userId;
    /**
     * 标签
     */
    private String tag;
    /**
     * 地图地址
     */
    private String mapUrl;
    /**
     * 0-正常，1-置顶，2-不可用
     */
    private Integer status;
    /**
     * 文章标题
     */
    private String title;
    /**
     * 下载数
     */
    private Long downloadCount;
    /**
     * 点赞数
     */
    private Long prefer;
    /**
     * 地图描述
     */
    private String description;
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