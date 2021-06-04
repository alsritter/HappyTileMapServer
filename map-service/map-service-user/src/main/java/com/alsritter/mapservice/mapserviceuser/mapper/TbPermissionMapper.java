package com.alsritter.mapservice.mapserviceuser.mapper;

import com.alsritter.mapserviceapi.mapserviceuserapi.entity.TbPermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 权限表(tb_permission)数据Mapper
 *
 * @author alsritter
 * @since 2021-06-02 00:37:39
 * @description auto generator
*/
@Mapper
public interface TbPermissionMapper extends BaseMapper<TbPermission> {
    /**
     * 根据用户 id 查询用户权限
     * @param userId 用户 ID
     * @return 当前用户的权限
     */
    @Select("SELECT *\n" +
            "FROM tb_permission\n" +
            "WHERE id IN (\n" +
            "    SELECT permission_id\n" +
            "    FROM tb_role_permission\n" +
            "    WHERE role_id IN (\n" +
            "        SELECT role_id\n" +
            "        FROM tb_user_role\n" +
            "        WHERE user_id = #{userId}\n" +
            "    )\n" +
            ");")
    List<TbPermission> findPermissionsByUserId(String userId);
}
