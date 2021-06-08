package com.alsritter.serviceapi.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Date;

/**
 * 权限表(tb_permission)实体类
 *
 * @author alsritter
 * @description auto generator
 * @since 2021-06-05 17:00:11
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("tb_permission")
public class TbPermission extends Model<TbPermission> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 父权限
     */
    private Long parentId;
    /**
     * 权限名称
     */
    private String name;
    /**
     * 权限英文名称
     */
    private String enname;
    /**
     * 授权路径
     */
    private String url;
    /**
     * 备注
     */
    private String description;
    /**
     * created
     */
    private Date created;
    /**
     * updated
     */
    private Date updated;
}