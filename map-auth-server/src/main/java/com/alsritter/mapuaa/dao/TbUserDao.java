package com.alsritter.mapuaa.dao;

import lombok.extern.slf4j.Slf4j;
import com.alsritter.mapuaa.entity.TbUser;
import com.alsritter.mapuaa.mapper.TbUserMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;

/**
 * 用户表(tb_user)数据DAO
 *
 * @author alsritter
 * @since 2021-06-02 00:37:39
 * @description auto generator
 */
@Slf4j
@Repository
public class TbUserDao extends ServiceImpl<TbUserMapper, TbUser> {

}