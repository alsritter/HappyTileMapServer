package com.alsritter.mapserviceapi.mapserviceuserapi.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 角色表(tb_role)实体类
 *
 * @author alsritter
 * @since 2021-06-02 00:37:39
 * @description auto generator
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("tb_role")
public class TbRole extends Model<TbRole> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
	private Long id;
    /**
     * 父角色
     */
    private Long parentId;
    /**
     * 角色名称
     */
    private String name;
    /**
     * 角色英文名称
     */
    private String enname;
    /**
     * 备注
     */
    private String description;
    /**
     * created
     */
    private LocalDateTime created;
    /**
     * updated
     */
    private LocalDateTime updated;
    /**
     * 删除标识（0-正常,1-删除）
     */
    private String delFlag;

}