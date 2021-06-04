package com.alsritter.mapservice.mapserviceuser.dao;

import com.alsritter.mapservice.mapserviceuser.mapper.TbUserMapper;
import com.alsritter.mapserviceapi.mapserviceuserapi.entity.TbUser;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * 用户表(tb_user)数据DAO
 *
 * @author alsritter
 * @description auto generator
 * @since 2021-06-02 00:37:39
 */
@Slf4j
@Repository
public class TbUserDao extends ServiceImpl<TbUserMapper, TbUser> {

}