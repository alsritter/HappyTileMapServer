package com.alsritter.mapuaa.dao;

import lombok.extern.slf4j.Slf4j;
import com.alsritter.mapuaa.entity.TbRole;
import com.alsritter.mapuaa.mapper.TbRoleMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;

/**
 * 角色表(tb_role)数据DAO
 *
 * @author alsritter
 * @since 2021-06-02 00:37:39
 * @description auto generator
 */
@Slf4j
@Repository
public class TbRoleDao extends ServiceImpl<TbRoleMapper, TbRole> {
}