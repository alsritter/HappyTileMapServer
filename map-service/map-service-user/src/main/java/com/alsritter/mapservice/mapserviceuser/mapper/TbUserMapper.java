package com.alsritter.mapservice.mapserviceuser.mapper;

import com.alsritter.mapserviceapi.mapserviceuserapi.entity.TbUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 用户表(tb_user)数据 Mapper
 *
 * @author alsritter
 * @description 查询角色
 * @since 2021-06-02 00:37:39
 */
@Mapper
public interface TbUserMapper extends BaseMapper<TbUser> {

    @Select("SELECT * FROM `tb_user` WHERE username = #{username}  limit 1")
    @Results({
            @Result(id = true, property = "id", column = "id"),
            // `column = "id"` 是指将 user 表中的用户主键 id 作为 `findPermissionsByUserId` 的查询参数
            @Result(property = "permissions", column = "id", javaType = List.class,
                    many = @Many(select = "com.alsritter.mapservice.mapserviceuser.mapper.TbPermissionMapper.findPermissionsByUserId"))
    })
    TbUser getUserByUsername(String username);
}
