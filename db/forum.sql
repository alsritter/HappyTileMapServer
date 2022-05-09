-- MySQL dump 10.13  Distrib 5.7.28, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: happy_map_forum
-- ------------------------------------------------------
-- Server version	5.7.31

/*!40101 SET @OLD_CHARACTER_SET_CLIENT = @@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS = @@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION = @@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE = @@TIME_ZONE */;
/*!40103 SET TIME_ZONE = '+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0 */;
/*!40101 SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES = @@SQL_NOTES, SQL_NOTES = 0 */;

--
-- Table structure for table `tb_comment`
--

DROP TABLE IF EXISTS `tb_comment`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_comment`
(
    `comment_id`       bigint(20)          NOT NULL AUTO_INCREMENT COMMENT '评论id',
    `floor`            tinyint(3) unsigned NOT NULL COMMENT '评论所在楼',
    `user_id`          bigint(20)          NOT NULL COMMENT '评论的人',
    `content`          varchar(140)                 DEFAULT NULL COMMENT '内容',
    `create_time`      timestamp           NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `last_modify_time` timestamp           NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `del_flag`         tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '0-正常，1-删除',
    PRIMARY KEY (`comment_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT ='评论表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_comment`
--

LOCK TABLES `tb_comment` WRITE;
/*!40000 ALTER TABLE `tb_comment`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_comment`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_comment_topic`
--

DROP TABLE IF EXISTS `tb_comment_topic`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_comment_topic`
(
    `id`         bigint(20)          NOT NULL AUTO_INCREMENT,
    `comment_id` bigint              not null comment '评论的 id',
    `master_id`  bigint(20)          NOT NULL COMMENT '这条评论依托的 map 表或 topic 表的 id',
    `flag`       tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '0-topic，1-map',
    `del_flag`   tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '0-正常，1-删除',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT ='评论中间表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_comment_topic`
--

LOCK TABLES `tb_comment_topic` WRITE;
/*!40000 ALTER TABLE `tb_comment_topic`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_comment_topic`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_map`
--

DROP TABLE IF EXISTS `tb_map`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_map`
(
    `map_id`           bigint(20)          NOT NULL AUTO_INCREMENT,
    `user_id`          bigint(20)          NOT NULL COMMENT '地图作者',
    `tag`              varchar(20)         NOT NULL COMMENT '标签',
    `map_url`          varchar(255)        NOT NULL COMMENT '地图地址',
    `status`           tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '0-正常，1-置顶，2-不可用',
    `title`            varchar(50)         NOT NULL COMMENT '文章标题',
    `download_count`   bigint(20)          NOT NULL DEFAULT '0' COMMENT '下载数',
    `prefer`           bigint(20)          NOT NULL DEFAULT '0' COMMENT '点赞数',
    `description`      varchar(5000)                DEFAULT NULL COMMENT '地图描述',
    `create_time`      timestamp           NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `last_modify_time` timestamp           NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `del_flag`         tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '0-正常，1-删除',
    PRIMARY KEY (`map_id`),
    KEY `title` (`title`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT ='地图表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_map`
--

LOCK TABLES `tb_map` WRITE;
/*!40000 ALTER TABLE `tb_map`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_map`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_topic`
--

DROP TABLE IF EXISTS `tb_topic`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_topic`
(
    `topic_id`         bigint(20)          NOT NULL AUTO_INCREMENT,
    `user_id`          bigint(20)          NOT NULL COMMENT '主题作者',
    `tag`              varchar(20)         NOT NULL COMMENT '标签',
    `status`           tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '0-正常，1-置顶，2-不可用',
    `title`            varchar(50)         NOT NULL COMMENT '文章标题',
    `content`          varchar(5000)                DEFAULT NULL COMMENT '内容',
    `browsed`          bigint(20)          NOT NULL DEFAULT '0' COMMENT '文章浏览量',
    `prefer`           bigint(20)          NOT NULL DEFAULT '0' COMMENT '点赞数',
    `create_time`      timestamp           NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `last_modify_time` timestamp           NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `del_flag`         tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '0-正常，1-删除',
    PRIMARY KEY (`topic_id`),
    KEY `title` (`title`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 3
  DEFAULT CHARSET = utf8 COMMENT ='主题表（或者说是文章）';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_topic`
--

LOCK TABLES `tb_topic` WRITE;
/*!40000 ALTER TABLE `tb_topic`
    DISABLE KEYS */;
INSERT INTO `tb_topic`
VALUES (1, 3, '分享', 0, '测试文章',
        '这是一篇测试文章的内容，以下随便生成的字，Lorem ipsum, dolor sit amet consectetur adipisicing elit. Quasi eaque, debitis accusamus cum exercitationem quas eius aspernatur illo fugit soluta culpa non fugiat molestias corporis. Similique eos ad ut! Iure!',
        0, 0, '2021-08-30 14:19:03', '2021-08-30 14:19:03', 0),
       (2, 48, '精品', 1, '测试文章置顶',
        '废话：Lorem ipsum dolor sit amet consectetur adipisicing elit. Perspiciatis, repellat. Tempora autem eaque alias impedit doloremque debitis consequuntur quo, illum ex at dicta possimus fugit nostrum repudiandae ipsum odio illo iste. Ratione quam perspiciatis, necessitatibus vero voluptas commodi ex aperiam architecto distinctio rerum facere asperiores aspernatur, adipisci at veritatis voluptates voluptate ipsam earum illum explicabo cum qui tempore! Ducimus sit iure similique libero ad enim facere. Quibusdam ex blanditiis vitae cumque culpa repellendus minima, voluptas ratione nemo aliquam? Architecto laudantium, odio consequatur quo rem saepe! Accusantium, vel, error natus quas, cum adipisci perspiciatis rerum architecto quasi dolore dolorem quidem ab voluptas corporis tempora! Eos, reiciendis sint facere harum totam modi officia aspernatur omnis nobis itaque ipsum non libero placeat dolor asperiores fugit illo sed? Eaque quos cumque cum delectus et dolorem doloribus at, velit nesciunt eum fugiat vel possimus, recusandae eveniet vero distinctio temporibus! Incidunt porro dolorum consequatur? Non assumenda, exercitationem recusandae aperiam ea perferendis alias quos dolorum sint commodi hic, fuga repellat veniam aliquam, repudiandae corrupti? Ullam dignissimos veritatis expedita nobis delectus. Molestiae repellendus alias cupiditate inventore aspernatur, perferendis omnis iste! Nulla ex est voluptatem omnis reprehenderit. Sapiente animi eum quae eaque, quo ipsam esse odio tempora voluptatem accusamus!',
        0, 0, '2021-08-30 14:20:19', '2021-08-30 14:20:19', 0);
/*!40000 ALTER TABLE `tb_topic`
    ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE = @OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE = @OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT = @OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS = @OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION = @OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES = @OLD_SQL_NOTES */;

-- Dump completed on 2021-08-31 17:13:55
