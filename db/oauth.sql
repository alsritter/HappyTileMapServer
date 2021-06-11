
-- ----------------------------
-- 下面的两张表还暂时用不上~
-- 等以后有空好好改造下
-- ----------------------------



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