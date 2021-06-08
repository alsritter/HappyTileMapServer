package com.alsritter.service.user.mapper;

import com.alsritter.serviceapi.user.entity.TbRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 角色表(tb_role)数据Mapper
 *
 * @author alsritter
 * @since 2021-06-05 17:00:11
 * @description auto generator
*/
@Mapper
public interface TbRoleMapper extends BaseMapper<TbRole> {
    /**
     * 根据用户 id 查询用户角色
     *
     * @param userId 用户 ID
     * @return 当前用户的权限
     */
    @Select("SELECT *\n" +
            "FROM tb_role\n" +
            "where id in (\n" +
            "    SELECT role_id\n" +
            "    FROM tb_user_role\n" +
            "    WHERE user_id = #{userId}\n" +
            ");")
    List<TbRole> findRoleByUserId(long userId);
}
