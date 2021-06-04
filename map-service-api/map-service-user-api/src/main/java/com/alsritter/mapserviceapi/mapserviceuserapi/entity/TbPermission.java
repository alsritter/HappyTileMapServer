package com.alsritter.mapserviceapi.mapserviceuserapi.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;

/**
 * 权限表(tb_permission)实体类
 *
 * @author alsritter
 * @since 2021-06-02 00:37:39
 * @description auto generator
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("tb_permission")
public class TbPermission extends Model<TbPermission> implements Serializable , GrantedAuthority {
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

    /**
     * 如果授予的权限可以当作一个String的话，就可以返回一个String
     *
     * @return 当前权限名称
     */
    @JsonIgnore
    @Override
    public String getAuthority() {
        return enname;
    }
}