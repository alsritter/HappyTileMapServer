DROP DATABASE IF EXISTS `happy_map`;

CREATE DATABASE `happy_map` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

USE `happy_map`;


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


-- ----------------------------
-- 用户表
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user`
(
    `id`          bigint(20)   NOT NULL AUTO_INCREMENT,
    `username`    varchar(50)  NOT NULL COMMENT '用户名',
    `password`    varchar(64)  NOT NULL COMMENT '密码，加密存储',
    `phone`       varchar(20)           DEFAULT NULL COMMENT '注册手机号',
    `email`       varchar(50)           DEFAULT NULL COMMENT '注册邮箱',
    `avatar`      varchar(255)          DEFAULT NULL COMMENT '头像地址',
    `description` varchar(200)          DEFAULT NULL COMMENT '个人信息',
    `created`     timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
    `updated`     timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
    `lock_flag`   char(1)      NOT NULL DEFAULT '0' COMMENT '0-正常，1-锁定',
    `del_flag`    char(1)      NOT NULL DEFAULT '0' COMMENT '0-正常，1-删除',
    PRIMARY KEY (`id`),
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


-- 注意，下面两张表不用管它的字段，它们都是 spring-oauth2.0 提供的标准字段，而且也无需自己查询这表，
-- spring-oauth2.0 提供了 JdbcClientDetailsService 和 JdbcAuthorizationCodeServices 工具类自动查询这个表
-- 所以只需按照标准创建这个表就行了

-- ----------------------------
-- 存放 oauth 的认证信息
-- ----------------------------
DROP TABLE IF EXISTS `oauth_client_details`;
CREATE TABLE `oauth_client_details`
(
    `client_id`               varchar(255) NOT NULL COMMENT '客户端标识',
    `resource_ids`            varchar(255) NULL     DEFAULT NULL COMMENT '接入资源列表',
    `client_secret`           varchar(255) NULL     DEFAULT NULL COMMENT '客户端秘钥',
    `scope`                   varchar(255) NULL     DEFAULT NULL COMMENT '客户端申请的权限范围',
    `authorized_grant_types`  varchar(255) NULL     DEFAULT NULL COMMENT '客户端支持的 grant_type',
    `web_server_redirect_uri` varchar(255) NULL     DEFAULT NULL COMMENT '重定向URI',
    `authorities`             varchar(255) NULL     DEFAULT NULL COMMENT '客户端所拥有的Spring Security的权限值，多个用逗号(,)分隔',
    `access_token_validity`   int(11)      NULL     DEFAULT NULL COMMENT '访问令牌有效时间值(单位:秒)',
    `refresh_token_validity`  int(11)      NULL     DEFAULT NULL COMMENT '更新令牌有效时间值(单位:秒)',
    `additional_information`  longtext     NULL     DEFAULT NULL COMMENT '预留字段',
    `create_time`             timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
    `archived`                tinyint(4)   NULL     DEFAULT NULL,
    `trusted`                 tinyint(4)   NULL     DEFAULT NULL,
    `autoapprove`             varchar(255) NULL     DEFAULT NULL COMMENT '用户是否自动Approval操作',
    PRIMARY KEY (`client_id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '接入客户端信息'
  ROW_FORMAT = Dynamic;


-- ----------------------------
-- 这里存放授权码
-- ----------------------------
DROP TABLE IF EXISTS `oauth_code`;
CREATE TABLE `oauth_code`
(
    `create_time`    timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
    `code`           varchar(255) NULL     DEFAULT NULL COMMENT '授权码(未加密)',
    `authentication` blob         NULL COMMENT 'AuthorizationRequestHolder.java对象序列化后的二进制数据',
    INDEX `code_index` (`code`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = Compact
    COMMENT ='用户数据库存取授权码模式存放授权码的，表中的字段也是固定的';