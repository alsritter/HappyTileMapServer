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


create table tb_map
(
    map_id           bigint(20) auto_increment,
    user_id          bigint(20)                                 not null comment '地图作者',
    tag              varchar(20)                                not null comment '标签',
    map_url          varchar(255)                               not null comment '地图地址',
    status           tinyint unsigned default 0                 not null comment '0-正常，1-置顶，2-不可用',
    title            varchar(50)                                not null comment '文章标题',
    download_count   bigint(20)       default 0                 not null comment '下载数',
    prefer           bigint(20)       default 0                 not null comment '点赞数',
    description      varchar(5000)                              null comment '地图描述',
    create_time      timestamp        default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    last_modify_time timestamp        default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    del_flag         tinyint unsigned default 0                 not null comment '0-正常，1-删除',
    primary key (`map_id`),
    INDEX (`title`)
) comment '地图表' charset = utf8;

-- 因为评论可能同时出现在 map 表和 topic 表，所以创建一个中间表用来标识
create table tb_comment_topic
(
    id        bigint(20) auto_increment,
    master_id bigint(20)                 not null comment 'map 表或 topic 表的 id',
    flag      tinyint unsigned default 0 not null comment '0-topic，1-map',
    del_flag  tinyint unsigned default 0 not null comment '0-正常，1-删除',
    primary key (`id`)
) comment '评论中间表' charset = utf8;

