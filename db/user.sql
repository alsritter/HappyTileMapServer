DROP DATABASE IF EXISTS `happy_map_user`;

CREATE DATABASE `happy_map_user` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;


USE `happy_map_user`;


/**
 * 如果源服务器上的 innodb_default_row_format 设置与目标服务器上的设置不同，则导入未明确定义行格式的 table 会导致架构不匹配错误。
 * 所以下面显示的指定了格式为 DYNAMIC
 * 而使用 Compact 格式存储，存储在其中的行数据会以 zlib 的算法进行压缩，因此对于 BLOB、TEXT、VARCHAR 这类大长度类型的数据能进行非常有效的存储。
 * 更多的 DYNAMIC 使用参考：https://www.docs4dev.com/docs/zh/mysql/5.7/reference/innodb-row-format.html
 */


-- ----------------------------
-- 创建权限表
-- ----------------------------
DROP TABLE IF EXISTS `tb_permission`;
CREATE TABLE `tb_permission`
(
    `id`          bigint(20)   NOT NULL AUTO_INCREMENT,
    `parent_id`   bigint(20)            DEFAULT NULL COMMENT '父权限',
    `name`        varchar(64)  NOT NULL COMMENT '权限名称',
    `enname`      varchar(64)  NOT NULL COMMENT '权限英文名称',
    `url`         varchar(255) NOT NULL COMMENT '授权路径',
    `description` varchar(200)          DEFAULT NULL COMMENT '备注',
    `created`     timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
    `updated`     timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 0
  DEFAULT CHARSET = utf8
  ROW_FORMAT = DYNAMIC COMMENT ='权限表';

INSERT INTO `tb_permission` value (1, null, '删除权限', 'DELETE', '', '删除权限', CURRENT_TIMESTAMP(0),CURRENT_TIMESTAMP(0));
INSERT INTO `tb_permission` value (2, null, '修改权限', 'UPDATE', '', '修改权限', CURRENT_TIMESTAMP(0),CURRENT_TIMESTAMP(0));
INSERT INTO `tb_permission` value (3, null, '添加数据权限', 'INSERT', '', '添加数据权限', CURRENT_TIMESTAMP(0),CURRENT_TIMESTAMP(0));
INSERT INTO `tb_permission` value (4, null, '查询权限', 'QUERY', '', '查询权限', CURRENT_TIMESTAMP(0),CURRENT_TIMESTAMP(0));

-- ----------------------------
-- 创建角色
-- ----------------------------
DROP TABLE IF EXISTS `tb_role`;
CREATE TABLE `tb_role`
(
    `id`          bigint(20)   NOT NULL AUTO_INCREMENT,
    `parent_id`   bigint(20)            DEFAULT NULL COMMENT '父角色',
    `name`        varchar(64)  NOT NULL COMMENT '角色名称',
    `enname`      varchar(64)  NOT NULL COMMENT '角色英文名称',
    `description` varchar(200)          DEFAULT NULL COMMENT '备注',
    `created`     timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
    `updated`     timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
    `del_flag`    char(1)      NOT NULL DEFAULT '0' COMMENT '删除标识（0-正常,1-删除）',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 0
  DEFAULT CHARSET = utf8
  ROW_FORMAT = DYNAMIC COMMENT ='角色表';

INSERT INTO `tb_role` VALUES (1, null, '管理员', 'admin', null, CURRENT_TIMESTAMP(0), CURRENT_TIMESTAMP(0), 0);
INSERT INTO `tb_role` VALUES (2, null, '一般用户', 'user', null, CURRENT_TIMESTAMP(0), CURRENT_TIMESTAMP(0), 0);

-- ----------------------------
-- 角色权限表（多对多）
-- ----------------------------
DROP TABLE IF EXISTS `tb_role_permission`;
CREATE TABLE `tb_role_permission`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT,
    `role_id`       bigint(20) NOT NULL COMMENT '角色 ID',
    `permission_id` bigint(20) NOT NULL COMMENT '权限 ID',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 0
  DEFAULT CHARSET = utf8
  ROW_FORMAT = DYNAMIC COMMENT ='角色权限表';

INSERT INTO `tb_role_permission` values (1 ,1 ,1);
INSERT INTO `tb_role_permission` values (2 ,1 ,2);
INSERT INTO `tb_role_permission` values (3 ,1 ,3);
INSERT INTO `tb_role_permission` values (4 ,1 ,4);
INSERT INTO `tb_role_permission` values (5 ,2 ,3);
INSERT INTO `tb_role_permission` values (6 ,2 ,4);


-- ----------------------------
-- 用户表
-- ----------------------------

create table tb_user
(
    user_id          bigint(20) auto_increment,
    username         varchar(50)                                not null comment '用户名（不能重复）',
    password         varchar(64)                                not null comment '密码，加密存储',
    phone            varchar(20)                                null comment '注册手机号（不能重复）',
    email            varchar(50)                                null comment '注册邮箱（不能重复）',
    avatar           varchar(255)                               null comment '头像地址',
    description      varchar(200)                               null comment '个人信息',
    gender           tinyint unsigned default 0                 not null comment '0-女，1-男, 2-保密，',
    create_time      timestamp        default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    last_modify_time timestamp        default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    status           tinyint unsigned default 0                 not null comment '0-正常，1-不可以评论，2-不可以登录',
    lock_flag        tinyint unsigned default 0                 not null comment '0-正常，1-锁定',
    del_flag         tinyint unsigned default 0                 not null comment '0-正常，1-删除',
    primary key (`user_id`),
    UNIQUE KEY `username` (`username`) USING BTREE, -- USING BTREE 使用 B树索引
    UNIQUE KEY `phone` (`phone`) USING BTREE,
    UNIQUE KEY `email` (`email`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 0
  DEFAULT CHARSET = utf8
  ROW_FORMAT = DYNAMIC COMMENT ='用户表';

-- ----------------------------
-- 用户角色表（多对多）
-- ----------------------------
DROP TABLE IF EXISTS `tb_user_role`;
CREATE TABLE `tb_user_role`
(
    `id`      bigint(20) NOT NULL AUTO_INCREMENT,
    `user_id` bigint(20) NOT NULL COMMENT '用户 ID',
    `role_id` bigint(20) NOT NULL COMMENT '角色 ID',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 0
  DEFAULT CHARSET = utf8
  ROW_FORMAT = DYNAMIC COMMENT ='用户角色表';

