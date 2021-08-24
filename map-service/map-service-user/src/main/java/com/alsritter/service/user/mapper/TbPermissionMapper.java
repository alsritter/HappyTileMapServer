package com.alsritter.service.user.mapper;

import com.alsritter.serviceapi.user.domain.UrlAndNameDo;
import com.alsritter.serviceapi.user.entity.TbPermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 权限表(tb_permission)数据Mapper
 *
 * @author alsritter
 * @description auto generator
 * @since 2021-06-05 17:00:11
 */
@Mapper
public interface TbPermissionMapper extends BaseMapper<TbPermission> {
    /**
     * 根据用户 id 查询用户权限
     *
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
    List<TbPermission> findPermissionsByUserId(long userId);

    @Select("SELECT url, enname\n" +
            "FROM tb_role_permission trp\n" +
            "         JOIN tb_permission tp ON tp.id = trp.permission_id\n" +
            "         JOIN tb_role tr ON tr.id = trp.role_id;")
    List<UrlAndNameDo> getAllPermissions();
}
