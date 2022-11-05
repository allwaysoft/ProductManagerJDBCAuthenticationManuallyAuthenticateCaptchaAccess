-- --------------------------------------------------------
-- 主机:                           127.0.0.1
-- 服务器版本:                        8.0.22 - MySQL Community Server - GPL
-- 服务器操作系统:                      Win64
-- HeidiSQL 版本:                  12.1.0.6537
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- 导出 product3 的数据库结构
CREATE DATABASE IF NOT EXISTS `product3` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `product3`;

-- 导出  表 product3.permission 结构
CREATE TABLE IF NOT EXISTS `permission` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `uri` varchar(8192) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `method` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 正在导出表  product3.permission 的数据：~5 rows (大约)
INSERT INTO `permission` (`id`, `name`, `description`, `uri`, `method`) VALUES
	(3, 'product_create', '增加产品', '/new', 'GET'),
	(4, 'product_delete', '删除产品', '/delete/*', 'GET'),
	(5, 'product_save', '保存产品', '/save', 'POST'),
	(9, 'product_read', '读取产品', '/', 'GET'),
	(10, 'product_edit', '编辑产品', '/edit/*', 'GET');

-- 导出  表 product3.product 结构
CREATE TABLE IF NOT EXISTS `product` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `brand` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `madein` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `price` float NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 正在导出表  product3.product 的数据：~2 rows (大约)
INSERT INTO `product` (`id`, `brand`, `madein`, `name`, `price`) VALUES
	(6, '6', '6', '6', 6),
	(7, '7', '7', '7', 7);

-- 导出  表 product3.role 结构
CREATE TABLE IF NOT EXISTS `role` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 正在导出表  product3.role 的数据：~3 rows (大约)
INSERT INTO `role` (`id`, `name`, `description`) VALUES
	(1, 'ADMIN', 'Administrator role'),
	(2, 'USER_P1', 'Perfil 1'),
	(3, 'USER_P2', 'Perfil 2');

-- 导出  表 product3.role_permission 结构
CREATE TABLE IF NOT EXISTS `role_permission` (
  `id` int NOT NULL AUTO_INCREMENT,
  `role_id` int NOT NULL DEFAULT '0',
  `permission_id` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `role_id_permission_id` (`role_id`,`permission_id`),
  KEY `FK_role_permission_permission` (`permission_id`),
  CONSTRAINT `FK_role_permission_permission` FOREIGN KEY (`permission_id`) REFERENCES `permission` (`id`),
  CONSTRAINT `FK_role_permission_role` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 正在导出表  product3.role_permission 的数据：~10 rows (大约)
INSERT INTO `role_permission` (`id`, `role_id`, `permission_id`) VALUES
	(1, 1, 3),
	(2, 1, 4),
	(3, 1, 5),
	(4, 1, 9),
	(5, 1, 10),
	(8, 2, 5),
	(6, 2, 9),
	(7, 2, 10),
	(10, 3, 4),
	(9, 3, 9);

-- 导出  表 product3.urls 结构
CREATE TABLE IF NOT EXISTS `urls` (
  `name` varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 正在导出表  product3.urls 的数据：~0 rows (大约)

-- 导出  表 product3.user 结构
CREATE TABLE IF NOT EXISTS `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `name` varchar(65) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `enabled` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 正在导出表  product3.user 的数据：~3 rows (大约)
INSERT INTO `user` (`id`, `username`, `email`, `name`, `password`, `enabled`) VALUES
	(1, 'admin', 'admin@example.com', 'Administrator', '$2a$10$2/LSmp3YoEOT97KzgrYODen7I88ErBovM2Qehw9DL1dW9DZ7DZSAm', 1),
	(2, 'u1', 'u1@example.com', 'User P1', '$2a$10$2/LSmp3YoEOT97KzgrYODen7I88ErBovM2Qehw9DL1dW9DZ7DZSAm', 1),
	(3, 'u2', 'u2@example.com', 'User P2', '$2a$10$2/LSmp3YoEOT97KzgrYODen7I88ErBovM2Qehw9DL1dW9DZ7DZSAm', 1);

-- 导出  表 product3.user_role 结构
CREATE TABLE IF NOT EXISTS `user_role` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL DEFAULT '0',
  `role_id` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id_role_id` (`user_id`,`role_id`),
  KEY `FK_user_role_role` (`role_id`),
  CONSTRAINT `FK_user_role_role` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`),
  CONSTRAINT `FK_user_role_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 正在导出表  product3.user_role 的数据：~3 rows (大约)
INSERT INTO `user_role` (`id`, `user_id`, `role_id`) VALUES
	(1, 1, 1),
	(2, 2, 2),
	(3, 3, 3);

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
