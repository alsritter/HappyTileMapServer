-- MySQL dump 10.13  Distrib 5.7.28, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: happy_map_user
-- ------------------------------------------------------
-- Server version	5.7.31

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `tb_permission`
--

DROP TABLE IF EXISTS `tb_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL COMMENT '权限名称',
  `url` varchar(255) NOT NULL COMMENT '授权路径',
  `description` varchar(200) DEFAULT NULL COMMENT '备注',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_public` tinyint(3) NOT NULL DEFAULT '0' COMMENT '0-私有资源 1-表示是开放资源（无需验证）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='权限表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_permission`
--

LOCK TABLES `tb_permission` WRITE;
/*!40000 ALTER TABLE `tb_permission` DISABLE KEYS */;
INSERT INTO `tb_permission` VALUES (1,'删除权限','/test/test','删除权限','2021-08-22 03:13:41','2021-08-22 03:13:41',0),(2,'取得实例','/discovery/instances','取得实例','2021-08-24 02:46:24','2021-08-24 02:46:24',0),(3,'测试权限','/api/hello','测试权限','2021-08-24 02:46:24','2021-08-24 02:46:24',0),(4,'取得用户信息','/forum/user/getuser','根据 Token 取得用户信息','2021-08-30 05:27:27','2021-08-30 05:27:27',1),(5,'取得全部主题','/forum/topics/get-all','取得全部主题','2021-08-30 15:14:21','2021-08-30 15:14:21',1),(6,'测试主题方法','/forum/topics/test','测试主题方法','2021-08-30 15:37:54','2021-08-30 15:37:54',1),(7,'根据 tag 取得主题','/forum/topics/get-by-tag','根据 tag 取得主题','2021-08-31 09:13:19','2021-08-31 09:13:19',1),(8,'根据 id 取得主题','/forum/topics/get-by-id','根据 id 取得主题','2021-08-31 09:13:19','2021-08-31 09:13:19',1),(9,'搜索主题','/forum/topics/search','搜索主题','2021-08-31 09:13:19','2021-08-31 09:13:19',1);
/*!40000 ALTER TABLE `tb_permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_role`
--

DROP TABLE IF EXISTS `tb_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父角色',
  `name` varchar(64) NOT NULL COMMENT '角色名称',
  `enname` varchar(64) NOT NULL COMMENT '角色英文名称',
  `description` varchar(200) DEFAULT NULL COMMENT '备注',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标识（0-正常,1-删除）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='角色表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_role`
--

LOCK TABLES `tb_role` WRITE;
/*!40000 ALTER TABLE `tb_role` DISABLE KEYS */;
INSERT INTO `tb_role` VALUES (1,NULL,'管理员','ADMIN',NULL,'2021-08-24 15:55:33','2021-08-24 15:55:33','0'),(2,NULL,'一般用户','USER','不能删，这个是基本 Role','2021-08-25 08:55:55','2021-08-25 08:55:55','0');
/*!40000 ALTER TABLE `tb_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_role_permission`
--

DROP TABLE IF EXISTS `tb_role_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_role_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) NOT NULL COMMENT '角色 ID',
  `permission_id` bigint(20) NOT NULL COMMENT '权限 ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='角色权限表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_role_permission`
--

LOCK TABLES `tb_role_permission` WRITE;
/*!40000 ALTER TABLE `tb_role_permission` DISABLE KEYS */;
INSERT INTO `tb_role_permission` VALUES (1,1,1),(2,2,1),(3,1,2),(4,2,2),(5,1,3);
/*!40000 ALTER TABLE `tb_role_permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_user`
--

DROP TABLE IF EXISTS `tb_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL COMMENT '用户名（不能重复）',
  `password` varchar(64) NOT NULL COMMENT '密码，加密存储',
  `phone` varchar(20) DEFAULT NULL COMMENT '注册手机号（不能重复）',
  `email` varchar(50) DEFAULT NULL COMMENT '注册邮箱（不能重复）',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像地址',
  `description` varchar(200) DEFAULT NULL COMMENT '个人信息',
  `gender` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '0-男，1-女, 2-保密，',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `last_modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `status` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '0-正常，1-不可以评论，2-不可以登录',
  `lock_flag` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '0-正常，1-锁定',
  `del_flag` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '0-正常，1-删除',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `username` (`username`) USING BTREE,
  UNIQUE KEY `phone` (`phone`) USING BTREE,
  UNIQUE KEY `email` (`email`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_user`
--

LOCK TABLES `tb_user` WRITE;
/*!40000 ALTER TABLE `tb_user` DISABLE KEYS */;
INSERT INTO `tb_user` VALUES (3,'alsritter','$2a$10$GjFNUGKaykLN/K.j/FG95u2vI0k5xl1jKHMPtA1DToW8PC4K9G0Mq','13128863336','a3320447910@qq.com',NULL,NULL,0,'2021-08-27 08:48:04','2021-08-27 08:48:04',0,0,0),(6,'alsritter02','$2a$10$hFcza..Hq8G60Bz2zlvYBe4WSJIOjm32GF.Kmwhz4.XdDYxAz4i/S','13128863333','3320447911@qq.com',NULL,NULL,0,'2021-08-26 10:35:38','2021-08-26 10:35:38',0,0,0),(44,'alsritter03','$2a$10$8RZ5DgOxoFoNKH8xDC1GAOj2FCToJqcRh8hkj6djk1QcC6RXtfSw6','13128863222','3320447912@qq.com',NULL,NULL,0,'2021-08-26 10:35:38','2021-08-26 10:35:38',0,0,0),(45,'alsritter04','$2a$10$WKmHCXdXc/Y1aDKc/4VTHeftjvCGk3DGIjWhDJHkQNKf5vrDEht5m','13128866666','3320447913@qq.com',NULL,NULL,0,'2021-08-26 10:35:38','2021-08-26 10:35:38',0,0,0),(46,'alsritter05','$2a$10$nbmtKykOB6XJse.m3.12KuRyi082YEG62/nICeGEK1bV6vgP12HBa','13128866677','3320447914@qq.com',NULL,NULL,0,'2021-08-26 10:35:38','2021-08-26 10:35:38',0,0,0),(47,'alsritter06','$2a$10$R4AxKtWJXJuhcV.qbV9nO.q8bqqfuGW949z31b2m4JJvO7EwxSISO','13128860007','3320447915@qq.com',NULL,NULL,0,'2021-08-26 10:35:38','2021-08-26 10:35:38',0,0,0),(48,'测试用户01','$2a$10$LniM6TUb6vmsaYVKM3vp8.TdA2Vik7eVVnp76yNvsAy0vgKKyeHK2','13128863330','3320447910@qq.com',NULL,NULL,0,'2021-08-27 16:52:11','2021-08-27 16:52:11',0,0,0);
/*!40000 ALTER TABLE `tb_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_user_role`
--

DROP TABLE IF EXISTS `tb_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_user_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT '用户 ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色 ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='用户角色表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_user_role`
--

LOCK TABLES `tb_user_role` WRITE;
/*!40000 ALTER TABLE `tb_user_role` DISABLE KEYS */;
INSERT INTO `tb_user_role` VALUES (1,3,2),(2,6,2),(3,44,2),(4,45,2),(5,46,2),(6,47,2),(7,48,2);
/*!40000 ALTER TABLE `tb_user_role` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-08-31 17:16:03
