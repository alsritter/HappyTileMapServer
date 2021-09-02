package com.alsritter.service.forum.mapper;

import com.alsritter.serviceapi.forum.entity.TbComment;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 评论表(tb_comment)数据Mapper
 *
 * @author alsritter
 * @description auto generator
 * @since 2021-08-15 11:30:49
 */
@Mapper
public interface TbCommentMapper extends BaseMapper<TbComment> {

    @Select("select *\n" +
            "from tb_comment\n" +
            "where comment_id in (\n" +
            "    select comment_id\n" +
            "    from tb_comment_topic\n" +
            "    where ${ew.customSqlSegment}\n" +
            ") order by floor")
    IPage<TbComment> getCommentPageByMasterId(Page<TbComment> page, @Param(Constants.WRAPPER) Wrapper<TbComment> wrapper);

    @Select("select *\n" +
            "from tb_comment\n" +
            "where comment_id in (\n" +
            "    select comment_id\n" +
            "    from tb_comment_topic\n" +
            "    where master_id = #{masterId} and flag = #{flag}\n" +
            ") order by floor")
    List<TbComment> getCommentByMasterId(@Param("masterId") Long masterId, @Param("flag") Integer flag);


    @Select("select count(comment_id) as `count`\n" +
            "from tb_comment\n" +
            "where comment_id in (\n" +
            "    select comment_id\n" +
            "    from tb_comment_topic\n" +
            "    where master_id = #{masterId} and flag = #{flag}\n" +
            ")")
    Integer getCommentCountByMasterId(@Param("masterId") Long masterId, @Param("flag") Integer flag);

    @Select("select MAX(floor)\n" +
            "from tb_comment\n" +
            "where comment_id in (\n" +
            "    select comment_id\n" +
            "    from tb_comment_topic\n" +
            "    where master_id = #{masterId} and flag = #{flag}\n" +
            ")")
    Integer getMaxFloor(@Param("masterId") Long masterId, @Param("flag") Integer flag);

    /**
     * 插入到中间表
     */
    @Insert("INSERT INTO happy_map_forum.tb_comment_topic (comment_id, master_id, flag, del_flag) " +
            "VALUES (#{commentId}, #{masterId}, #{flag}, DEFAULT)")
    int instarMiddleTable(@Param("commentId") Long commentId,@Param("masterId") Long masterId, @Param("flag") Integer flag);
}
