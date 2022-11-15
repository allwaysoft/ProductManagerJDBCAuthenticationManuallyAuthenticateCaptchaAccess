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
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 正在导出表  product3.permission 的数据：~28 rows (大约)
INSERT INTO `permission` (`id`, `name`, `description`, `uri`, `method`) VALUES
	(3, 'product_create', '增加产品', '/product/new', 'GET'),
	(4, 'product_delete', '删除产品', '/product/delete/*', 'GET'),
	(5, 'product_save', '保存产品', '/product/save', 'POST'),
	(9, 'product', '列表产品', '/product', 'GET'),
	(10, 'product_edit', '编辑产品', '/product/edit/*', 'GET'),
	(11, 'register', '注册用户', '/register', 'GET'),
	(12, 'process_register', '保存用户', '/process_register', 'POST'),
	(13, 'user', '列表用户', '/user', 'GET'),
	(14, 'user/info', 'user/info', '/user/info', 'GET'),
	(15, 'user_save', '保存用户', '/user/save', 'POST'),
	(16, 'user_edit', '编辑用户', '/user/edit/*', 'GET'),
	(17, 'permission', '读取权限', '/permission', 'GET'),
	(18, 'permission_create', '增加权限', '/permission/new', 'GET'),
	(19, 'permission_delete', '删除权限', '/permission/delete/*', 'GET'),
	(20, 'permission_edit', '编辑权限', '/permission/edit/*', 'GET'),
	(21, 'permission_save', '保存权限', '/permission/save', 'POST'),
	(23, 'test1', '测试1', '/test1', 'GET1'),
	(24, 'user_delete', '删除用户', '/user/delete/*', 'GET'),
	(25, 'user_create', '增加用户', '/user/new', 'GET'),
	(26, 'role', '读取角色', '/role', 'GET'),
	(27, 'role_create', '创建角色', '/role/new', 'GET'),
	(28, 'role_save', '保存角色', '/role/save', 'POST'),
	(29, 'role_edit', '编辑角色', '/role/edit/*', 'GET'),
	(30, 'role_delete', '删除角色', '/role/delete/*', 'GET'),
	(31, 'user_update', '更新用户（不含密码）', '/user/update', 'POST'),
	(32, 'user_resetpassword', '重置密码', '/user/resetpassword/*', 'GET'),
	(33, 'user_savepassword', '保存重置密码', '/user/savepassword', 'POST'),
	(34, 'order_create', '创建订单', '/order/new', 'GET');

-- 导出  表 product3.product 结构
CREATE TABLE IF NOT EXISTS `product` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `brand` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `madein` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `price` float NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 正在导出表  product3.product 的数据：~5 rows (大约)
INSERT INTO `product` (`id`, `brand`, `madein`, `name`, `price`) VALUES
	(6, '6', '6', '6', 6),
	(7, '7', '7', '7', 7),
	(9, '66', '66', '66', 66),
	(10, '88', '887', '88', 88);

-- 导出  表 product3.role 结构
CREATE TABLE IF NOT EXISTS `role` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

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
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 正在导出表  product3.role_permission 的数据：~31 rows (大约)
INSERT INTO `role_permission` (`id`, `role_id`, `permission_id`) VALUES
	(1, 1, 3),
	(2, 1, 4),
	(3, 1, 5),
	(4, 1, 9),
	(5, 1, 10),
	(11, 1, 11),
	(12, 1, 12),
	(13, 1, 13),
	(14, 1, 14),
	(16, 1, 15),
	(15, 1, 16),
	(17, 1, 17),
	(18, 1, 18),
	(19, 1, 19),
	(20, 1, 20),
	(22, 1, 21),
	(23, 1, 24),
	(24, 1, 25),
	(25, 1, 26),
	(26, 1, 27),
	(29, 1, 28),
	(28, 1, 29),
	(27, 1, 30),
	(35, 1, 31),
	(36, 1, 32),
	(37, 1, 33),
	(8, 2, 5),
	(6, 2, 9),
	(7, 2, 10),
	(10, 3, 4),
	(9, 3, 9);

-- 导出  表 product3.user 结构
CREATE TABLE IF NOT EXISTS `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `name` varchar(65) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `enabled` int DEFAULT '1',
  `homepage` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '',
  `password_changed_time` datetime(6) DEFAULT NULL,
  `account_non_locked` bit(1) DEFAULT b'1',
  `failed_attempt` int DEFAULT '0',
  `lock_time` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 正在导出表  product3.user 的数据：~9 rows (大约)
INSERT INTO `user` (`id`, `username`, `email`, `name`, `password`, `enabled`, `homepage`, `password_changed_time`, `account_non_locked`, `failed_attempt`, `lock_time`) VALUES
	(1, 'admin', 'admin@admin.com', '系统管理员', '$2a$10$rouJbLys6oKzcXb1clpz0OKEtADYfbFvPOkb6UzX.9z/CLW6hTiqG', 1, '', '2022-11-15 15:34:39.329000', b'1', 0, NULL),
	(2, 'u1', 'u1@example.com', 'User P1', '$2a$10$7lsSw2BeeDCmm6gYJBdJfuotFoDkfggWq8pWTjj7awbwywNdGHZ3u', 1, 'home', NULL, b'1', 0, NULL),
	(3, 'u2', 'u2@example.com', 'User P2', '$2a$10$2/LSmp3YoEOT97KzgrYODen7I88ErBovM2Qehw9DL1dW9DZ7DZSAm', 1, '', NULL, b'1', 0, NULL),
	(11, 'allway222', 'allway222@1.com', 'allway222', '$2a$10$WTg/WDKmYaYZUbFl39Z6au78WOG8LI9bcnvbbLs/.t5MjzwrBD60u', 1, 'home', NULL, b'1', 0, NULL),
	(12, 'allway666', 'allway666@1.com', 'allway66622', '$2a$10$Jeky6hyUSN3i0u6IPF8squDtjkFwHL7WzP3rRoF4QOEwWfVAzi/c2', 1, 'home', NULL, b'1', 0, NULL),
	(13, 'allway777', 'allway777@1.com', 'allway777', '$2a$10$W0TWw1.9lBYG7PW8KsEWcuNFGiOJAOobr55bKMQD8rf68GVppUH4S', 1, 'home', NULL, b'1', 0, NULL),
	(14, 'test', '1@1.com', 'test', '$2a$10$Kq1sx9/uoAedR2LsUKoVQeUfY8Qho9afSKuGLj4u9Y9iTRo//17Fq', 1, 'home', NULL, b'1', 0, NULL),
	(25, 'user102', 'Password@123.com', 'user102', '$2a$10$hZvr4Ao7ofNFdXHjPkRWXuCuuUUJmImhJjVAiiWxTBKRF2Rx5Z8RG', 1, '', '2022-11-15 15:17:05.924000', b'1', 0, NULL),
	(26, 'user103', 'Password@123.com', 'user103', '$2a$10$XOOH81X1bdmOAzK154o3JOhsM2bfe70A3BQCyHwgUoWJxon39I8QS', 1, '', '2022-11-15 15:35:58.437000', b'1', 0, NULL);

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
) ENGINE=InnoDB AUTO_INCREMENT=65 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 正在导出表  product3.user_role 的数据：~14 rows (大约)
INSERT INTO `user_role` (`id`, `user_id`, `role_id`) VALUES
	(59, 1, 1),
	(58, 1, 2),
	(57, 1, 3),
	(2, 2, 2),
	(3, 3, 3),
	(43, 11, 1),
	(44, 12, 1),
	(26, 13, 1),
	(51, 14, 1),
	(52, 14, 3),
	(60, 25, 1),
	(64, 26, 1),
	(63, 26, 2),
	(62, 26, 3);

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
