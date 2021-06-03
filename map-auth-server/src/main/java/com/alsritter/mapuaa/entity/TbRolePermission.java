package com.alsritter.mapuaa.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 角色权限表(tb_role_permission)实体类
 *
 * @author alsritter
 * @since 2021-06-02 00:37:39
 * @description auto generator
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("tb_role_permission")
public class TbRolePermission extends Model<TbRolePermission> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
	private Long id;
    /**
     * 角色 ID
     */
    private Long roleId;
    /**
     * 权限 ID
     */
    private Long permissionId;

}