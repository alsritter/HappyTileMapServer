DROP DATABASE IF EXISTS `happy_map_forum`;

CREATE DATABASE `happy_map_forum` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

USE `happy_map_forum`;


create table tb_comment
(
    comment_id       bigint(20) auto_increment comment '评论id',
    floor            tinyint unsigned                           not null comment '评论所在楼',
    user_id          bigint(20)                                 not null comment '评论的人',
    content          varchar(140)                               null comment '内容',
    create_time      timestamp        default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    last_modify_time timestamp        default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    del_flag         tinyint unsigned default 0                 not null comment '0-正常，1-删除',
    primary key (`comment_id`)
) comment '评论表' charset = utf8;


-- topic
create table tb_topic
(
    topic_id         bigint(20) auto_increment,
    user_id          bigint(20)                                 not null comment '主题作者',
    tag              varchar(20)                                not null comment '标签',
    status           tinyint unsigned default 0                 not null comment '0-正常，1-置顶，2-不可用',
    title            varchar(50)                                not null comment '文章标题',
    content          varchar(5000)                              null comment '内容',
    browsed          bigint(20)       default 0                 not null comment '文章浏览量',
    prefer           bigint(20)       default 0                 not null comment '点赞数',
    create_time      timestamp        default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    last_modify_time timestamp        default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    del_flag         tinyint unsigned default 0                 not null comment '0-正常，1-删除',
    primary key (`topic_id`),
    INDEX (`title`)
) comment '主题表（或者说是文章）' charset = utf8;