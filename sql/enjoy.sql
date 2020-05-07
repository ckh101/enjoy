/*
 Navicat Premium Data Transfer

 Source Server         : 本地数据库
 Source Server Type    : MySQL
 Source Server Version : 50722
 Source Host           : localhost:3306
 Source Schema         : enjoy

 Target Server Type    : MySQL
 Target Server Version : 50722
 File Encoding         : 65001

 Date: 07/05/2020 18:45:09
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for enjoy_industry
-- ----------------------------
DROP TABLE IF EXISTS `enjoy_industry`;
CREATE TABLE `enjoy_industry`  (
  `id` int(11) NOT NULL,
  `parentId` int(11) NULL DEFAULT NULL,
  `industry` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of enjoy_industry
-- ----------------------------
INSERT INTO `enjoy_industry` VALUES (1, 0, '电商', '2019-05-20 16:10:18', '2019-05-20 16:10:18');
INSERT INTO `enjoy_industry` VALUES (11, 1, '手表', '2019-05-20 17:30:19', '2019-05-20 17:30:19');
INSERT INTO `enjoy_industry` VALUES (12, 1, '女装', '2019-05-20 17:43:01', '2019-05-20 17:43:01');
INSERT INTO `enjoy_industry` VALUES (13, 1, '男装', '2019-05-20 17:43:08', '2019-05-20 17:43:08');

-- ----------------------------
-- Table structure for enjoy_keygen
-- ----------------------------
DROP TABLE IF EXISTS `enjoy_keygen`;
CREATE TABLE `enjoy_keygen`  (
  `table_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `last_id` int(10) UNSIGNED NOT NULL,
  PRIMARY KEY (`table_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of enjoy_keygen
-- ----------------------------
INSERT INTO `enjoy_keygen` VALUES ('enjoy_industry', 10);
INSERT INTO `enjoy_keygen` VALUES ('enjoy_module', 235);
INSERT INTO `enjoy_keygen` VALUES ('enjoy_role', 80);
INSERT INTO `enjoy_keygen` VALUES ('enjoy_user', 352);
INSERT INTO `enjoy_keygen` VALUES ('enjoy_wxuser', 150);

-- ----------------------------
-- Table structure for enjoy_module
-- ----------------------------
DROP TABLE IF EXISTS `enjoy_module`;
CREATE TABLE `enjoy_module`  (
  `id` int(11) NOT NULL,
  `pid` int(11) NOT NULL,
  `mname` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `menuicon` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `url` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `level` int(11) NULL DEFAULT NULL,
  `mtype` int(11) NULL DEFAULT NULL,
  `seq` int(11) NULL DEFAULT NULL,
  `mstatus` int(11) NULL DEFAULT NULL,
  `remark` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of enjoy_module
-- ----------------------------
INSERT INTO `enjoy_module` VALUES (1, 1, 'root', 'icon-display4', '#', -1, 0, 1, 1, '根', '2016-07-27 11:42:15', '2016-12-13 10:11:59');
INSERT INTO `enjoy_module` VALUES (2, 1, '系统管理', 'icon-display4', '#', 1, 1, 9, 1, '', NULL, '2019-05-31 17:52:09');
INSERT INTO `enjoy_module` VALUES (3, 2, '模块列表', 'icon-menu7', '/admin/module', 2, 1, 1, 1, '模块列表', '2017-01-29 21:56:24', '2017-01-29 21:56:24');
INSERT INTO `enjoy_module` VALUES (4, 2, '角色列表', 'icon-users2', '/admin/role', 2, 1, 2, 1, '角色列表', '2017-01-29 21:56:44', '2017-01-29 21:56:44');
INSERT INTO `enjoy_module` VALUES (5, 2, '用户列表', 'icon-user-check', '/admin/user', 2, 1, 3, 1, '用户列表', '2017-05-27 17:31:49', '2017-05-27 17:59:15');
INSERT INTO `enjoy_module` VALUES (76, 3, '模块编辑', '#', '/admin/module/edit', 3, 3, 1, 1, '', NULL, NULL);
INSERT INTO `enjoy_module` VALUES (77, 3, '模块删除', '#', '/admin/module/del', 3, 3, 2, 1, '', NULL, NULL);
INSERT INTO `enjoy_module` VALUES (78, 3, '模块新增', '#', '/admin/module/add', 3, 3, 3, 1, '', NULL, NULL);
INSERT INTO `enjoy_module` VALUES (79, 4, '角色编辑', '#', '/admin/role/edit', 3, 3, 1, 1, '', NULL, NULL);
INSERT INTO `enjoy_module` VALUES (80, 4, '角色删除', '#', '/admin/role/del', 3, 3, 2, 1, '', NULL, NULL);
INSERT INTO `enjoy_module` VALUES (81, 4, '角色新增', '#', '/admin/role/add', 3, 3, 3, 1, '', NULL, NULL);
INSERT INTO `enjoy_module` VALUES (82, 5, '用户编辑', '#', '/admin/user/edit', 3, 3, 1, 1, '', NULL, NULL);
INSERT INTO `enjoy_module` VALUES (83, 5, '用户删除', '#', '/admin/user/del', 3, 3, 2, 1, '', NULL, NULL);
INSERT INTO `enjoy_module` VALUES (84, 5, '用户新增', '#', '/admin/user/add', 3, 3, 3, 1, '', NULL, NULL);
INSERT INTO `enjoy_module` VALUES (86, 3, '模块查看', '#', '/admin/module', 3, 3, 4, 1, '', NULL, NULL);
INSERT INTO `enjoy_module` VALUES (87, 4, '角色查看', '#', '/admin/role', 3, 3, 4, 1, '', NULL, NULL);
INSERT INTO `enjoy_module` VALUES (88, 5, '用户查看', '#', '/admin/user', 3, 3, 4, 1, '', NULL, NULL);
INSERT INTO `enjoy_module` VALUES (106, 5, '重置密码', '#', '/admin/user/resetpassword', 3, 3, 5, 1, '', NULL, NULL);

-- ----------------------------
-- Table structure for enjoy_role
-- ----------------------------
DROP TABLE IF EXISTS `enjoy_role`;
CREATE TABLE `enjoy_role`  (
  `id` int(11) NOT NULL,
  `role_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `flag_str` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of enjoy_role
-- ----------------------------
INSERT INTO `enjoy_role` VALUES (31, '运营', 'operator', '2019-09-09 10:09:40', '2019-09-09 10:09:40');
INSERT INTO `enjoy_role` VALUES (41, '管理员', 'sys', '2019-10-09 12:09:29', '2019-10-09 12:09:29');

-- ----------------------------
-- Table structure for enjoy_role_module
-- ----------------------------
DROP TABLE IF EXISTS `enjoy_role_module`;
CREATE TABLE `enjoy_role_module`  (
  `role_id` int(11) NOT NULL,
  `module_id` int(11) NOT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of enjoy_role_module
-- ----------------------------
INSERT INTO `enjoy_role_module` VALUES (42, 1);
INSERT INTO `enjoy_role_module` VALUES (42, 126);
INSERT INTO `enjoy_role_module` VALUES (42, 167);
INSERT INTO `enjoy_role_module` VALUES (42, 188);
INSERT INTO `enjoy_role_module` VALUES (43, 1);
INSERT INTO `enjoy_role_module` VALUES (43, 126);
INSERT INTO `enjoy_role_module` VALUES (43, 167);
INSERT INTO `enjoy_role_module` VALUES (43, 188);
INSERT INTO `enjoy_role_module` VALUES (52, 1);
INSERT INTO `enjoy_role_module` VALUES (52, 126);
INSERT INTO `enjoy_role_module` VALUES (52, 167);
INSERT INTO `enjoy_role_module` VALUES (52, 187);
INSERT INTO `enjoy_role_module` VALUES (52, 188);
INSERT INTO `enjoy_role_module` VALUES (53, 1);
INSERT INTO `enjoy_role_module` VALUES (53, 126);
INSERT INTO `enjoy_role_module` VALUES (53, 167);
INSERT INTO `enjoy_role_module` VALUES (53, 187);
INSERT INTO `enjoy_role_module` VALUES (53, 188);
INSERT INTO `enjoy_role_module` VALUES (53, 2);
INSERT INTO `enjoy_role_module` VALUES (53, 5);
INSERT INTO `enjoy_role_module` VALUES (53, 82);
INSERT INTO `enjoy_role_module` VALUES (53, 83);
INSERT INTO `enjoy_role_module` VALUES (53, 84);
INSERT INTO `enjoy_role_module` VALUES (53, 88);
INSERT INTO `enjoy_role_module` VALUES (53, 106);
INSERT INTO `enjoy_role_module` VALUES (31, 1);
INSERT INTO `enjoy_role_module` VALUES (31, 126);
INSERT INTO `enjoy_role_module` VALUES (31, 167);
INSERT INTO `enjoy_role_module` VALUES (31, 187);
INSERT INTO `enjoy_role_module` VALUES (31, 188);
INSERT INTO `enjoy_role_module` VALUES (31, 177);
INSERT INTO `enjoy_role_module` VALUES (31, 178);
INSERT INTO `enjoy_role_module` VALUES (31, 179);
INSERT INTO `enjoy_role_module` VALUES (31, 180);
INSERT INTO `enjoy_role_module` VALUES (31, 181);
INSERT INTO `enjoy_role_module` VALUES (31, 182);
INSERT INTO `enjoy_role_module` VALUES (31, 2);
INSERT INTO `enjoy_role_module` VALUES (31, 5);
INSERT INTO `enjoy_role_module` VALUES (31, 88);
INSERT INTO `enjoy_role_module` VALUES (62, 1);
INSERT INTO `enjoy_role_module` VALUES (62, 197);
INSERT INTO `enjoy_role_module` VALUES (41, 1);
INSERT INTO `enjoy_role_module` VALUES (41, 126);
INSERT INTO `enjoy_role_module` VALUES (41, 167);
INSERT INTO `enjoy_role_module` VALUES (41, 187);
INSERT INTO `enjoy_role_module` VALUES (41, 188);
INSERT INTO `enjoy_role_module` VALUES (41, 177);
INSERT INTO `enjoy_role_module` VALUES (41, 178);
INSERT INTO `enjoy_role_module` VALUES (41, 179);
INSERT INTO `enjoy_role_module` VALUES (41, 180);
INSERT INTO `enjoy_role_module` VALUES (41, 181);
INSERT INTO `enjoy_role_module` VALUES (41, 182);
INSERT INTO `enjoy_role_module` VALUES (41, 197);
INSERT INTO `enjoy_role_module` VALUES (41, 207);
INSERT INTO `enjoy_role_module` VALUES (41, 208);
INSERT INTO `enjoy_role_module` VALUES (41, 209);
INSERT INTO `enjoy_role_module` VALUES (41, 210);
INSERT INTO `enjoy_role_module` VALUES (41, 211);
INSERT INTO `enjoy_role_module` VALUES (41, 212);
INSERT INTO `enjoy_role_module` VALUES (41, 213);
INSERT INTO `enjoy_role_module` VALUES (41, 214);
INSERT INTO `enjoy_role_module` VALUES (41, 2);
INSERT INTO `enjoy_role_module` VALUES (41, 3);
INSERT INTO `enjoy_role_module` VALUES (41, 76);
INSERT INTO `enjoy_role_module` VALUES (41, 77);
INSERT INTO `enjoy_role_module` VALUES (41, 78);
INSERT INTO `enjoy_role_module` VALUES (41, 86);
INSERT INTO `enjoy_role_module` VALUES (41, 4);
INSERT INTO `enjoy_role_module` VALUES (41, 79);
INSERT INTO `enjoy_role_module` VALUES (41, 80);
INSERT INTO `enjoy_role_module` VALUES (41, 81);
INSERT INTO `enjoy_role_module` VALUES (41, 87);
INSERT INTO `enjoy_role_module` VALUES (41, 5);
INSERT INTO `enjoy_role_module` VALUES (41, 82);
INSERT INTO `enjoy_role_module` VALUES (41, 83);
INSERT INTO `enjoy_role_module` VALUES (41, 84);
INSERT INTO `enjoy_role_module` VALUES (41, 88);

-- ----------------------------
-- Table structure for enjoy_user
-- ----------------------------
DROP TABLE IF EXISTS `enjoy_user`;
CREATE TABLE `enjoy_user`  (
  `id` int(11) NOT NULL,
  `account` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `user_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `company` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `system_status` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `account_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `operator_id` int(11) NULL DEFAULT NULL,
  `is_root` int(11) NULL DEFAULT 0,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  `last_login_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of enjoy_user
-- ----------------------------
INSERT INTO `enjoy_user` VALUES (1, 'admin', '超级管理员', 'a0777add3f9c42af3847f8a18726e2f1', '15889961234', '广东和邦网络科技有限公司', 'ALLOW', 'SYSTEM', NULL, 1, '2016-07-27 11:42:15', '2019-09-12 14:43:16', '2019-09-12 14:43:16');

-- ----------------------------
-- Table structure for enjoy_user_industry
-- ----------------------------
DROP TABLE IF EXISTS `enjoy_user_industry`;
CREATE TABLE `enjoy_user_industry`  (
  `userId` int(11) NULL DEFAULT NULL,
  `industry` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for enjoy_user_role
-- ----------------------------
DROP TABLE IF EXISTS `enjoy_user_role`;
CREATE TABLE `enjoy_user_role`  (
  `role_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`role_id`, `user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of enjoy_user_role
-- ----------------------------
INSERT INTO `enjoy_user_role` VALUES (31, 102);
INSERT INTO `enjoy_user_role` VALUES (31, 123);
INSERT INTO `enjoy_user_role` VALUES (31, 132);
INSERT INTO `enjoy_user_role` VALUES (31, 244);
INSERT INTO `enjoy_user_role` VALUES (31, 245);
INSERT INTO `enjoy_user_role` VALUES (31, 264);
INSERT INTO `enjoy_user_role` VALUES (41, 112);
INSERT INTO `enjoy_user_role` VALUES (41, 146);
INSERT INTO `enjoy_user_role` VALUES (41, 255);
INSERT INTO `enjoy_user_role` VALUES (41, 274);
INSERT INTO `enjoy_user_role` VALUES (41, 284);
INSERT INTO `enjoy_user_role` VALUES (41, 294);
INSERT INTO `enjoy_user_role` VALUES (41, 304);
INSERT INTO `enjoy_user_role` VALUES (41, 305);
INSERT INTO `enjoy_user_role` VALUES (41, 314);
INSERT INTO `enjoy_user_role` VALUES (41, 324);
INSERT INTO `enjoy_user_role` VALUES (41, 325);
INSERT INTO `enjoy_user_role` VALUES (41, 334);

-- ----------------------------
-- Table structure for enjoy_wxuser
-- ----------------------------
DROP TABLE IF EXISTS `enjoy_wxuser`;
CREATE TABLE `enjoy_wxuser`  (
  `id` int(11) NOT NULL,
  `user_id` int(11) NULL DEFAULT NULL,
  `open_id` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `union_id` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `nick_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `head_url` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `sex` int(11) NULL DEFAULT NULL,
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `token` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `refresh_token` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of enjoy_wxuser
-- ----------------------------
INSERT INTO `enjoy_wxuser` VALUES (1, 1, 'o8ESZ1PCCa5bjcT7K6O1THEsDIcY', 'o7LWu0tbbTTroC1a0NgyqVOlVMms', '浩子', 'http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83eoXibk4hrcibozzJZDXeQibRYELld8DjMBN0lkCyQjJn98odjHsSiaDK0RAEJpX2xkzBzic6TzytOVahaQ/132', 1, '15889963035', '25_WuY9pbDY0kn1MR87wpy_LhxHdp-8VDTIbPABNf2hVxKXMTX9QlbTj7hvKNl3ISbwrgferNsik_LOg6LvckUKQ1taCNQUoR8DjmwrTc2bTfM', '25_NSSNSk-T8R48YNhs91R2hBfPFe8knIEcuBriF3AHq15lINy8MuR9D47nd7zpZSU6x4T1ojWB57_54k2o7zqYHkkwbYBNlLLnGx-J-cUUZPE', NULL, '2019-09-12 14:43:16');

-- ----------------------------
-- Table structure for job_execution_log
-- ----------------------------
DROP TABLE IF EXISTS `job_execution_log`;
CREATE TABLE `job_execution_log`  (
  `id` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `job_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `task_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `hostname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `sharding_item` int(11) NOT NULL,
  `execution_source` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `failure_cause` varchar(4000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `is_success` int(11) NOT NULL,
  `start_time` timestamp(0) NULL DEFAULT NULL,
  `complete_time` timestamp(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of job_execution_log
-- ----------------------------
INSERT INTO `job_execution_log` VALUES ('0008d768-527f-4d42-9856-bfc925fd88f5', 'com.ckh.enjoy.web.service.job.SimpleJobDemo', 'com.ckh.enjoy.web.service.job.SimpleJobDemo@-@0@-@READY@-@169.254.171.155@-@14112', 'LAPTOP-7660VK7G', '169.254.171.155', 0, 'NORMAL_TRIGGER', NULL, 1, '2020-05-07 15:29:58', '2020-05-07 15:29:58');
INSERT INTO `job_execution_log` VALUES ('0f5ba9b0-cbe4-41a9-8395-326a4a37afbf', 'com.ckh.enjoy.web.service.job.SimpleJobDemo', 'com.ckh.enjoy.web.service.job.SimpleJobDemo@-@0@-@READY@-@169.254.171.155@-@14112', 'LAPTOP-7660VK7G', '169.254.171.155', 0, 'NORMAL_TRIGGER', NULL, 1, '2020-05-07 15:29:54', '2020-05-07 15:29:54');
INSERT INTO `job_execution_log` VALUES ('45ee32e9-8700-4340-8a2d-4ccc83ede0e0', 'com.ckh.enjoy.web.service.job.SimpleJobDemo', 'com.ckh.enjoy.web.service.job.SimpleJobDemo@-@0@-@READY@-@169.254.171.155@-@10892', 'LAPTOP-7660VK7G', '169.254.171.155', 0, 'NORMAL_TRIGGER', NULL, 1, '2020-05-07 15:24:22', '2020-05-07 15:24:22');
INSERT INTO `job_execution_log` VALUES ('4d2c0bfd-1904-4f5a-a862-b931644bad5e', 'com.ckh.enjoy.web.service.job.SimpleJobDemo', 'com.ckh.enjoy.web.service.job.SimpleJobDemo@-@0@-@READY@-@169.254.171.155@-@10892', 'LAPTOP-7660VK7G', '169.254.171.155', 0, 'NORMAL_TRIGGER', NULL, 1, '2020-05-07 15:24:21', '2020-05-07 15:24:21');
INSERT INTO `job_execution_log` VALUES ('6a66759c-0b7a-4492-aa88-964af5b2ca79', 'com.ckh.enjoy.web.service.job.SimpleJobDemo', 'com.ckh.enjoy.web.service.job.SimpleJobDemo@-@0@-@READY@-@169.254.171.155@-@14112', 'LAPTOP-7660VK7G', '169.254.171.155', 0, 'NORMAL_TRIGGER', NULL, 1, '2020-05-07 15:29:56', '2020-05-07 15:29:56');
INSERT INTO `job_execution_log` VALUES ('6a8c57c7-9209-4c6f-a117-aaf71e323a1d', 'com.ckh.enjoy.web.service.job.SimpleJobDemo', 'com.ckh.enjoy.web.service.job.SimpleJobDemo@-@0@-@READY@-@169.254.171.155@-@10892', 'LAPTOP-7660VK7G', '169.254.171.155', 0, 'NORMAL_TRIGGER', NULL, 1, '2020-05-07 15:24:19', '2020-05-07 15:24:19');
INSERT INTO `job_execution_log` VALUES ('703a1e6a-33d3-4223-8489-8366e4630501', 'com.ckh.enjoy.web.service.job.SimpleJobDemo', 'com.ckh.enjoy.web.service.job.SimpleJobDemo@-@0@-@READY@-@169.254.171.155@-@10892', 'LAPTOP-7660VK7G', '169.254.171.155', 0, 'NORMAL_TRIGGER', NULL, 1, '2020-05-07 15:24:19', '2020-05-07 15:24:19');
INSERT INTO `job_execution_log` VALUES ('7ad396d9-94ff-4bac-a799-a8ab7b695c8b', 'com.ckh.enjoy.web.service.job.SimpleJobDemo', 'com.ckh.enjoy.web.service.job.SimpleJobDemo@-@0,1@-@READY@-@169.254.171.155@-@53016', 'LAPTOP-7660VK7G', '169.254.171.155', 1, 'NORMAL_TRIGGER', NULL, 1, '2020-05-07 15:09:54', '2020-05-07 15:09:54');
INSERT INTO `job_execution_log` VALUES ('80262423-1143-4b14-a357-017eb366a0d1', 'com.ckh.enjoy.web.service.job.SimpleJobDemo', 'com.ckh.enjoy.web.service.job.SimpleJobDemo@-@0@-@READY@-@169.254.171.155@-@10892', 'LAPTOP-7660VK7G', '169.254.171.155', 0, 'NORMAL_TRIGGER', NULL, 1, '2020-05-07 15:24:20', '2020-05-07 15:24:20');
INSERT INTO `job_execution_log` VALUES ('8891ae96-bc9a-4645-8dba-d0514cae4318', 'com.ckh.enjoy.web.service.job.SimpleJobDemo', 'com.ckh.enjoy.web.service.job.SimpleJobDemo@-@0@-@READY@-@169.254.171.155@-@10892', 'LAPTOP-7660VK7G', '169.254.171.155', 0, 'NORMAL_TRIGGER', NULL, 1, '2020-05-07 15:24:28', '2020-05-07 15:24:28');
INSERT INTO `job_execution_log` VALUES ('903fc2f2-9e00-4203-bda1-141bba19f8f7', 'com.ckh.enjoy.web.service.job.SimpleJobDemo', 'com.ckh.enjoy.web.service.job.SimpleJobDemo@-@0@-@READY@-@169.254.171.155@-@14112', 'LAPTOP-7660VK7G', '169.254.171.155', 0, 'NORMAL_TRIGGER', NULL, 1, '2020-05-07 15:29:54', '2020-05-07 15:29:54');
INSERT INTO `job_execution_log` VALUES ('9eac10e9-8a99-4ce4-a879-63d2d39ad435', 'com.ckh.enjoy.web.service.job.SimpleJobDemo', 'com.ckh.enjoy.web.service.job.SimpleJobDemo@-@0@-@READY@-@169.254.171.155@-@14112', 'LAPTOP-7660VK7G', '169.254.171.155', 0, 'NORMAL_TRIGGER', NULL, 1, '2020-05-07 15:29:57', '2020-05-07 15:29:57');
INSERT INTO `job_execution_log` VALUES ('a570241d-09c6-495e-ba75-3e24a9d0a5fd', 'com.ckh.enjoy.web.service.job.SimpleJobDemo', 'com.ckh.enjoy.web.service.job.SimpleJobDemo@-@0@-@READY@-@169.254.171.155@-@14112', 'LAPTOP-7660VK7G', '169.254.171.155', 0, 'NORMAL_TRIGGER', NULL, 1, '2020-05-07 15:29:53', '2020-05-07 15:29:53');
INSERT INTO `job_execution_log` VALUES ('a579c793-5c69-4954-a365-f764cc73526d', 'com.ckh.enjoy.web.service.job.SimpleJobDemo', 'com.ckh.enjoy.web.service.job.SimpleJobDemo@-@0@-@READY@-@169.254.171.155@-@10892', 'LAPTOP-7660VK7G', '169.254.171.155', 0, 'NORMAL_TRIGGER', NULL, 1, '2020-05-07 15:24:23', '2020-05-07 15:24:23');
INSERT INTO `job_execution_log` VALUES ('b1dca4fb-3d26-40bf-bf37-bdab81c0f66b', 'com.ckh.enjoy.web.service.job.SimpleJobDemo', 'com.ckh.enjoy.web.service.job.SimpleJobDemo@-@0@-@READY@-@169.254.171.155@-@10892', 'LAPTOP-7660VK7G', '169.254.171.155', 0, 'NORMAL_TRIGGER', NULL, 1, '2020-05-07 15:24:26', '2020-05-07 15:24:26');
INSERT INTO `job_execution_log` VALUES ('b8e37ea4-ceec-4516-b9a0-06a71e818bcc', 'com.ckh.enjoy.web.service.job.SimpleJobDemo', 'com.ckh.enjoy.web.service.job.SimpleJobDemo@-@0@-@READY@-@169.254.171.155@-@10892', 'LAPTOP-7660VK7G', '169.254.171.155', 0, 'NORMAL_TRIGGER', NULL, 1, '2020-05-07 15:24:25', '2020-05-07 15:24:25');
INSERT INTO `job_execution_log` VALUES ('d0c0b4a4-1a3c-43a9-9151-193e30cd7d94', 'com.ckh.enjoy.web.service.job.SimpleJobDemo', 'com.ckh.enjoy.web.service.job.SimpleJobDemo@-@0@-@READY@-@169.254.171.155@-@14112', 'LAPTOP-7660VK7G', '169.254.171.155', 0, 'NORMAL_TRIGGER', NULL, 1, '2020-05-07 15:29:55', '2020-05-07 15:29:55');
INSERT INTO `job_execution_log` VALUES ('dbc0407e-79b5-4c48-8f4d-88f25d96b670', 'com.ckh.enjoy.web.service.job.SimpleJobDemo', 'com.ckh.enjoy.web.service.job.SimpleJobDemo@-@0@-@READY@-@169.254.171.155@-@10892', 'LAPTOP-7660VK7G', '169.254.171.155', 0, 'NORMAL_TRIGGER', NULL, 1, '2020-05-07 15:24:24', '2020-05-07 15:24:24');
INSERT INTO `job_execution_log` VALUES ('f5e25c44-1de2-4c72-a7ff-a4118d82cd5c', 'com.ckh.enjoy.web.service.job.SimpleJobDemo', 'com.ckh.enjoy.web.service.job.SimpleJobDemo@-@0@-@READY@-@169.254.171.155@-@14112', 'LAPTOP-7660VK7G', '169.254.171.155', 0, 'NORMAL_TRIGGER', NULL, 1, '2020-05-07 15:29:59', '2020-05-07 15:29:59');
INSERT INTO `job_execution_log` VALUES ('f97d98d8-7ade-4a20-8a96-f7bbc3cb3f1f', 'com.ckh.enjoy.web.service.job.SimpleJobDemo', 'com.ckh.enjoy.web.service.job.SimpleJobDemo@-@0@-@READY@-@169.254.171.155@-@10892', 'LAPTOP-7660VK7G', '169.254.171.155', 0, 'NORMAL_TRIGGER', NULL, 1, '2020-05-07 15:24:27', '2020-05-07 15:24:27');
INSERT INTO `job_execution_log` VALUES ('ff247fda-5d03-4875-84b3-50f7e2766cd2', 'com.ckh.enjoy.web.service.job.SimpleJobDemo', 'com.ckh.enjoy.web.service.job.SimpleJobDemo@-@0,1@-@READY@-@169.254.171.155@-@53016', 'LAPTOP-7660VK7G', '169.254.171.155', 0, 'NORMAL_TRIGGER', NULL, 1, '2020-05-07 15:09:54', '2020-05-07 15:09:54');

-- ----------------------------
-- Table structure for job_status_trace_log
-- ----------------------------
DROP TABLE IF EXISTS `job_status_trace_log`;
CREATE TABLE `job_status_trace_log`  (
  `id` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `job_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `original_task_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `task_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `slave_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `source` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `execution_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `sharding_item` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `state` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `message` varchar(4000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `creation_time` timestamp(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `TASK_ID_STATE_INDEX`(`task_id`, `state`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of job_status_trace_log
-- ----------------------------
INSERT INTO `job_status_trace_log` VALUES ('09f38fdc-71d8-4cda-a0e4-161fa467d537', 'com.ckh.enjoy.web.service.job.SimpleJobDemo', '', 'com.ckh.enjoy.web.service.job.SimpleJobDemo@-@0@-@READY@-@169.254.171.155@-@10892', '169.254.171.155', 'LITE_EXECUTOR', 'READY', '[0]', 'TASK_RUNNING', '', '2020-05-07 15:24:19');
INSERT INTO `job_status_trace_log` VALUES ('120c467c-6fb2-434d-9984-5416582f8a1e', 'com.ckh.enjoy.web.service.job.SimpleJobDemo', '', 'com.ckh.enjoy.web.service.job.SimpleJobDemo@-@0@-@READY@-@169.254.171.155@-@10892', '169.254.171.155', 'LITE_EXECUTOR', 'READY', '[0]', 'TASK_FINISHED', '', '2020-05-07 15:24:25');
INSERT INTO `job_status_trace_log` VALUES ('1304ac22-7501-4d88-befe-39933f4f01fb', 'com.ckh.enjoy.web.service.job.SimpleJobDemo', '', 'com.ckh.enjoy.web.service.job.SimpleJobDemo@-@0@-@READY@-@169.254.171.155@-@10892', '169.254.171.155', 'LITE_EXECUTOR', 'READY', '[0]', 'TASK_FINISHED', '', '2020-05-07 15:24:21');
INSERT INTO `job_status_trace_log` VALUES ('185cceca-5ee9-4751-89c4-7afa05352ba1', 'com.ckh.enjoy.web.service.job.SimpleJobDemo', '', 'com.ckh.enjoy.web.service.job.SimpleJobDemo@-@0@-@READY@-@169.254.171.155@-@14112', '169.254.171.155', 'LITE_EXECUTOR', 'READY', '[0]', 'TASK_STAGING', 'Job \'com.ckh.enjoy.web.service.job.SimpleJobDemo\' execute begin.', '2020-05-07 15:29:54');
INSERT INTO `job_status_trace_log` VALUES ('2064f93d-6b25-472c-b5c1-d1f06d2de42e', 'com.ckh.enjoy.web.service.job.SimpleJobDemo', '', 'com.ckh.enjoy.web.service.job.SimpleJobDemo@-@0@-@READY@-@169.254.171.155@-@14112', '169.254.171.155', 'LITE_EXECUTOR', 'READY', '[0]', 'TASK_STAGING', 'Job \'com.ckh.enjoy.web.service.job.SimpleJobDemo\' execute begin.', '2020-05-07 15:29:59');
INSERT INTO `job_status_trace_log` VALUES ('20f9d61b-0d54-496a-b2ce-5f3ce99941f5', 'com.ckh.enjoy.web.service.job.SimpleJobDemo', '', 'com.ckh.enjoy.web.service.job.SimpleJobDemo@-@0@-@READY@-@169.254.171.155@-@10892', '169.254.171.155', 'LITE_EXECUTOR', 'READY', '[0]', 'TASK_FINISHED', '', '2020-05-07 15:24:19');
INSERT INTO `job_status_trace_log` VALUES ('215dd05b-d022-4f44-a993-a2c3eb3b4254', 'com.ckh.enjoy.web.service.job.SimpleJobDemo', '', 'com.ckh.enjoy.web.service.job.SimpleJobDemo@-@0@-@READY@-@169.254.171.155@-@10892', '169.254.171.155', 'LITE_EXECUTOR', 'READY', '[0]', 'TASK_RUNNING', '', '2020-05-07 15:24:22');
INSERT INTO `job_status_trace_log` VALUES ('226ffa3e-63df-4555-b63a-b95e24180c08', 'com.ckh.enjoy.web.service.job.SimpleJobDemo', '', 'com.ckh.enjoy.web.service.job.SimpleJobDemo@-@0@-@READY@-@169.254.171.155@-@10892', '169.254.171.155', 'LITE_EXECUTOR', 'READY', '[0]', 'TASK_RUNNING', '', '2020-05-07 15:24:24');
INSERT INTO `job_status_trace_log` VALUES ('2d237aa0-f930-427d-b460-bc9adde220ec', 'com.ckh.enjoy.web.service.job.SimpleJobDemo', '', 'com.ckh.enjoy.web.service.job.SimpleJobDemo@-@0@-@READY@-@169.254.171.155@-@10892', '169.254.171.155', 'LITE_EXECUTOR', 'READY', '[0]', 'TASK_STAGING', 'Job \'com.ckh.enjoy.web.service.job.SimpleJobDemo\' execute begin.', '2020-05-07 15:24:26');
INSERT INTO `job_status_trace_log` VALUES ('34e49bf8-e4fe-467b-bcdc-ef9c77062483', 'com.ckh.enjoy.web.service.job.SimpleJobDemo', '', 'com.ckh.enjoy.web.service.job.SimpleJobDemo@-@0@-@READY@-@169.254.171.155@-@10892', '169.254.171.155', 'LITE_EXECUTOR', 'READY', '[0]', 'TASK_FINISHED', '', '2020-05-07 15:24:28');
INSERT INTO `job_status_trace_log` VALUES ('371e29f0-24bd-4071-b246-31a4858ed946', 'com.ckh.enjoy.web.service.job.SimpleJobDemo', '', 'com.ckh.enjoy.web.service.job.SimpleJobDemo@-@0@-@READY@-@169.254.171.155@-@14112', '169.254.171.155', 'LITE_EXECUTOR', 'READY', '[0]', 'TASK_FINISHED', '', '2020-05-07 15:29:55');
INSERT INTO `job_status_trace_log` VALUES ('465cea98-50e7-4475-bb4d-5205a7a2bd00', 'com.ckh.enjoy.web.service.job.SimpleJobDemo', '', 'com.ckh.enjoy.web.service.job.SimpleJobDemo@-@0@-@READY@-@169.254.171.155@-@10892', '169.254.171.155', 'LITE_EXECUTOR', 'READY', '[0]', 'TASK_STAGING', 'Job \'com.ckh.enjoy.web.service.job.SimpleJobDemo\' execute begin.', '2020-05-07 15:24:28');
INSERT INTO `job_status_trace_log` VALUES ('4bd94aed-2adc-4678-8966-49fab02f0d52', 'com.ckh.enjoy.web.service.job.SimpleJobDemo', '', 'com.ckh.enjoy.web.service.job.SimpleJobDemo@-@0@-@READY@-@169.254.171.155@-@10892', '169.254.171.155', 'LITE_EXECUTOR', 'READY', '[0]', 'TASK_FINISHED', '', '2020-05-07 15:24:27');
INSERT INTO `job_status_trace_log` VALUES ('4d641780-2434-4bb3-bc8e-514a141b0846', 'com.ckh.enjoy.web.service.job.SimpleJobDemo', '', 'com.ckh.enjoy.web.service.job.SimpleJobDemo@-@0@-@READY@-@169.254.171.155@-@10892', '169.254.171.155', 'LITE_EXECUTOR', 'READY', '[0]', 'TASK_RUNNING', '', '2020-05-07 15:24:26');
INSERT INTO `job_status_trace_log` VALUES ('5671287a-b744-4445-a9ab-16d4639f009d', 'com.ckh.enjoy.web.service.job.SimpleJobDemo', '', 'com.ckh.enjoy.web.service.job.SimpleJobDemo@-@0@-@READY@-@169.254.171.155@-@10892', '169.254.171.155', 'LITE_EXECUTOR', 'READY', '[0]', 'TASK_FINISHED', '', '2020-05-07 15:24:24');
INSERT INTO `job_status_trace_log` VALUES ('584c9be4-ef60-4209-9934-7ca7881d8685', 'com.ckh.enjoy.web.service.job.SimpleJobDemo', '', 'com.ckh.enjoy.web.service.job.SimpleJobDemo@-@0@-@READY@-@169.254.171.155@-@10892', '169.254.171.155', 'LITE_EXECUTOR', 'READY', '[0]', 'TASK_FINISHED', '', '2020-05-07 15:24:20');
INSERT INTO `job_status_trace_log` VALUES ('5a0c32e1-85c5-43f4-bc7e-a4bf3b7d356d', 'com.ckh.enjoy.web.service.job.SimpleJobDemo', '', 'com.ckh.enjoy.web.service.job.SimpleJobDemo@-@0@-@READY@-@169.254.171.155@-@14112', '169.254.171.155', 'LITE_EXECUTOR', 'READY', '[0]', 'TASK_RUNNING', '', '2020-05-07 15:29:58');
INSERT INTO `job_status_trace_log` VALUES ('5f26aa41-355c-47a6-8fc8-701c7db085de', 'com.ckh.enjoy.web.service.job.SimpleJobDemo', '', 'com.ckh.enjoy.web.service.job.SimpleJobDemo@-@0@-@READY@-@169.254.171.155@-@10892', '169.254.171.155', 'LITE_EXECUTOR', 'READY', '[0]', 'TASK_STAGING', 'Job \'com.ckh.enjoy.web.service.job.SimpleJobDemo\' execute begin.', '2020-05-07 15:24:25');
INSERT INTO `job_status_trace_log` VALUES ('6478545a-4f8a-4e60-a41f-ac1b4119a84f', 'com.ckh.enjoy.web.service.job.SimpleJobDemo', '', 'com.ckh.enjoy.web.service.job.SimpleJobDemo@-@0@-@READY@-@169.254.171.155@-@14112', '169.254.171.155', 'LITE_EXECUTOR', 'READY', '[0]', 'TASK_STAGING', 'Job \'com.ckh.enjoy.web.service.job.SimpleJobDemo\' execute begin.', '2020-05-07 15:29:52');
INSERT INTO `job_status_trace_log` VALUES ('64d06c62-f7c7-467d-ad71-a1cd2b0e1675', 'com.ckh.enjoy.web.service.job.SimpleJobDemo', '', 'com.ckh.enjoy.web.service.job.SimpleJobDemo@-@0@-@READY@-@169.254.171.155@-@10892', '169.254.171.155', 'LITE_EXECUTOR', 'READY', '[0]', 'TASK_FINISHED', '', '2020-05-07 15:24:19');
INSERT INTO `job_status_trace_log` VALUES ('69a98c1a-c5fc-40dd-8192-12ce18d3c337', 'com.ckh.enjoy.web.service.job.SimpleJobDemo', '', 'com.ckh.enjoy.web.service.job.SimpleJobDemo@-@0@-@READY@-@169.254.171.155@-@10892', '169.254.171.155', 'LITE_EXECUTOR', 'READY', '[0]', 'TASK_STAGING', 'Job \'com.ckh.enjoy.web.service.job.SimpleJobDemo\' execute begin.', '2020-05-07 15:24:27');
INSERT INTO `job_status_trace_log` VALUES ('6dca1400-37f1-4747-aaa4-1f6810a1571a', 'com.ckh.enjoy.web.service.job.SimpleJobDemo', '', 'com.ckh.enjoy.web.service.job.SimpleJobDemo@-@0@-@READY@-@169.254.171.155@-@10892', '169.254.171.155', 'LITE_EXECUTOR', 'READY', '[0]', 'TASK_RUNNING', '', '2020-05-07 15:24:27');
INSERT INTO `job_status_trace_log` VALUES ('7043d8eb-ede5-40ad-95c6-60587da5f5ef', 'com.ckh.enjoy.web.service.job.SimpleJobDemo', '', 'com.ckh.enjoy.web.service.job.SimpleJobDemo@-@0@-@READY@-@169.254.171.155@-@14112', '169.254.171.155', 'LITE_EXECUTOR', 'READY', '[0]', 'TASK_STAGING', 'Job \'com.ckh.enjoy.web.service.job.SimpleJobDemo\' execute begin.', '2020-05-07 15:29:54');
INSERT INTO `job_status_trace_log` VALUES ('709abe09-a481-4e18-954e-0e33acb5747e', 'com.ckh.enjoy.web.service.job.SimpleJobDemo', '', 'com.ckh.enjoy.web.service.job.SimpleJobDemo@-@0@-@READY@-@169.254.171.155@-@10892', '169.254.171.155', 'LITE_EXECUTOR', 'READY', '[0]', 'TASK_RUNNING', '', '2020-05-07 15:24:21');
INSERT INTO `job_status_trace_log` VALUES ('7a055463-aecc-47b4-a7f2-cf81a5f8d48f', 'com.ckh.enjoy.web.service.job.SimpleJobDemo', '', 'com.ckh.enjoy.web.service.job.SimpleJobDemo@-@0@-@READY@-@169.254.171.155@-@14112', '169.254.171.155', 'LITE_EXECUTOR', 'READY', '[0]', 'TASK_STAGING', 'Job \'com.ckh.enjoy.web.service.job.SimpleJobDemo\' execute begin.', '2020-05-07 15:29:55');
INSERT INTO `job_status_trace_log` VALUES ('7c05f75c-802b-43a1-9c81-2615ca12ee10', 'com.ckh.enjoy.web.service.job.SimpleJobDemo', '', 'com.ckh.enjoy.web.service.job.SimpleJobDemo@-@0@-@READY@-@169.254.171.155@-@14112', '169.254.171.155', 'LITE_EXECUTOR', 'READY', '[0]', 'TASK_FINISHED', '', '2020-05-07 15:29:58');
INSERT INTO `job_status_trace_log` VALUES ('7d7384f3-626a-4630-a512-a18e2c808ac5', 'com.ckh.enjoy.web.service.job.SimpleJobDemo', '', 'com.ckh.enjoy.web.service.job.SimpleJobDemo@-@0@-@READY@-@169.254.171.155@-@10892', '169.254.171.155', 'LITE_EXECUTOR', 'READY', '[0]', 'TASK_STAGING', 'Job \'com.ckh.enjoy.web.service.job.SimpleJobDemo\' execute begin.', '2020-05-07 15:24:22');
INSERT INTO `job_status_trace_log` VALUES ('7e6514ac-73d6-4653-8f8c-0d3d724c0b73', 'com.ckh.enjoy.web.service.job.SimpleJobDemo', '', 'com.ckh.enjoy.web.service.job.SimpleJobDemo@-@0@-@READY@-@169.254.171.155@-@14112', '169.254.171.155', 'LITE_EXECUTOR', 'READY', '[0]', 'TASK_RUNNING', '', '2020-05-07 15:29:56');
INSERT INTO `job_status_trace_log` VALUES ('815bb054-fd76-4f30-85e8-272f2ff59003', 'com.ckh.enjoy.web.service.job.SimpleJobDemo', '', 'com.ckh.enjoy.web.service.job.SimpleJobDemo@-@0@-@READY@-@169.254.171.155@-@10892', '169.254.171.155', 'LITE_EXECUTOR', 'READY', '[0]', 'TASK_RUNNING', '', '2020-05-07 15:24:25');
INSERT INTO `job_status_trace_log` VALUES ('88ea4637-a63c-4e62-ae2f-06c9ba989ca1', 'com.ckh.enjoy.web.service.job.SimpleJobDemo', '', 'com.ckh.enjoy.web.service.job.SimpleJobDemo@-@0@-@READY@-@169.254.171.155@-@14112', '169.254.171.155', 'LITE_EXECUTOR', 'READY', '[0]', 'TASK_FINISHED', '', '2020-05-07 15:29:57');
INSERT INTO `job_status_trace_log` VALUES ('8aba3c2e-e0b8-4b15-a51b-80092258d302', 'com.ckh.enjoy.web.service.job.SimpleJobDemo', '', 'com.ckh.enjoy.web.service.job.SimpleJobDemo@-@0@-@READY@-@169.254.171.155@-@10892', '169.254.171.155', 'LITE_EXECUTOR', 'READY', '[0]', 'TASK_STAGING', 'Job \'com.ckh.enjoy.web.service.job.SimpleJobDemo\' execute begin.', '2020-05-07 15:24:24');
INSERT INTO `job_status_trace_log` VALUES ('8d32885f-3ba7-4013-a4b7-09af75cbeb30', 'com.ckh.enjoy.web.service.job.SimpleJobDemo', '', 'com.ckh.enjoy.web.service.job.SimpleJobDemo@-@0,1@-@READY@-@169.254.171.155@-@53016', '169.254.171.155', 'LITE_EXECUTOR', 'READY', '[0, 1]', 'TASK_RUNNING', '', '2020-05-07 15:09:54');
INSERT INTO `job_status_trace_log` VALUES ('911ff1ab-75f1-4ce4-a7b6-593a5d1a5521', 'com.ckh.enjoy.web.service.job.SimpleJobDemo', '', 'com.ckh.enjoy.web.service.job.SimpleJobDemo@-@0@-@READY@-@169.254.171.155@-@10892', '169.254.171.155', 'LITE_EXECUTOR', 'READY', '[0]', 'TASK_RUNNING', '', '2020-05-07 15:24:20');
INSERT INTO `job_status_trace_log` VALUES ('91cb0122-b616-4248-a0ab-2a930b747859', 'com.ckh.enjoy.web.service.job.SimpleJobDemo', '', 'com.ckh.enjoy.web.service.job.SimpleJobDemo@-@0@-@READY@-@169.254.171.155@-@14112', '169.254.171.155', 'LITE_EXECUTOR', 'READY', '[0]', 'TASK_STAGING', 'Job \'com.ckh.enjoy.web.service.job.SimpleJobDemo\' execute begin.', '2020-05-07 15:29:56');
INSERT INTO `job_status_trace_log` VALUES ('9d714fcb-f7dd-4cd3-8917-57d6d86c2b1d', 'com.ckh.enjoy.web.service.job.SimpleJobDemo', '', 'com.ckh.enjoy.web.service.job.SimpleJobDemo@-@0,1@-@READY@-@169.254.171.155@-@53016', '169.254.171.155', 'LITE_EXECUTOR', 'READY', '[0, 1]', 'TASK_FINISHED', '', '2020-05-07 15:09:54');
INSERT INTO `job_status_trace_log` VALUES ('9dc0ebc2-03de-41f5-9eba-84a70331137e', 'com.ckh.enjoy.web.service.job.SimpleJobDemo', '', 'com.ckh.enjoy.web.service.job.SimpleJobDemo@-@0@-@READY@-@169.254.171.155@-@10892', '169.254.171.155', 'LITE_EXECUTOR', 'READY', '[0]', 'TASK_STAGING', 'Job \'com.ckh.enjoy.web.service.job.SimpleJobDemo\' execute begin.', '2020-05-07 15:24:18');
INSERT INTO `job_status_trace_log` VALUES ('a752d605-0eb8-4962-bb7f-d00b112460e0', 'com.ckh.enjoy.web.service.job.SimpleJobDemo', '', 'com.ckh.enjoy.web.service.job.SimpleJobDemo@-@0@-@READY@-@169.254.171.155@-@10892', '169.254.171.155', 'LITE_EXECUTOR', 'READY', '[0]', 'TASK_FINISHED', '', '2020-05-07 15:24:22');
INSERT INTO `job_status_trace_log` VALUES ('aa1a6823-4bbc-4e59-ad16-0108d852314e', 'com.ckh.enjoy.web.service.job.SimpleJobDemo', '', 'com.ckh.enjoy.web.service.job.SimpleJobDemo@-@0@-@READY@-@169.254.171.155@-@14112', '169.254.171.155', 'LITE_EXECUTOR', 'READY', '[0]', 'TASK_RUNNING', '', '2020-05-07 15:29:59');
INSERT INTO `job_status_trace_log` VALUES ('af5af388-072a-4cb2-ad2c-ac19fcedecc9', 'com.ckh.enjoy.web.service.job.SimpleJobDemo', '', 'com.ckh.enjoy.web.service.job.SimpleJobDemo@-@0@-@READY@-@169.254.171.155@-@10892', '169.254.171.155', 'LITE_EXECUTOR', 'READY', '[0]', 'TASK_RUNNING', '', '2020-05-07 15:24:19');
INSERT INTO `job_status_trace_log` VALUES ('afb3eae1-d346-45d4-afe7-d66a600ad4b7', 'com.ckh.enjoy.web.service.job.SimpleJobDemo', '', 'com.ckh.enjoy.web.service.job.SimpleJobDemo@-@0@-@READY@-@169.254.171.155@-@14112', '169.254.171.155', 'LITE_EXECUTOR', 'READY', '[0]', 'TASK_FINISHED', '', '2020-05-07 15:29:54');
INSERT INTO `job_status_trace_log` VALUES ('b5874bb4-55eb-49b3-974f-5c8b2d70e804', 'com.ckh.enjoy.web.service.job.SimpleJobDemo', '', 'com.ckh.enjoy.web.service.job.SimpleJobDemo@-@0@-@READY@-@169.254.171.155@-@14112', '169.254.171.155', 'LITE_EXECUTOR', 'READY', '[0]', 'TASK_FINISHED', '', '2020-05-07 15:29:54');
INSERT INTO `job_status_trace_log` VALUES ('b80635a7-516d-4713-8676-d13ffe6af2a2', 'com.ckh.enjoy.web.service.job.SimpleJobDemo', '', 'com.ckh.enjoy.web.service.job.SimpleJobDemo@-@0@-@READY@-@169.254.171.155@-@14112', '169.254.171.155', 'LITE_EXECUTOR', 'READY', '[0]', 'TASK_RUNNING', '', '2020-05-07 15:29:54');
INSERT INTO `job_status_trace_log` VALUES ('bf93720c-f8b7-40fe-99c7-06347e51a0f9', 'com.ckh.enjoy.web.service.job.SimpleJobDemo', '', 'com.ckh.enjoy.web.service.job.SimpleJobDemo@-@0@-@READY@-@169.254.171.155@-@10892', '169.254.171.155', 'LITE_EXECUTOR', 'READY', '[0]', 'TASK_STAGING', 'Job \'com.ckh.enjoy.web.service.job.SimpleJobDemo\' execute begin.', '2020-05-07 15:24:23');
INSERT INTO `job_status_trace_log` VALUES ('c0586701-b039-47ca-b628-e3e02ef1068d', 'com.ckh.enjoy.web.service.job.SimpleJobDemo', '', 'com.ckh.enjoy.web.service.job.SimpleJobDemo@-@0@-@READY@-@169.254.171.155@-@10892', '169.254.171.155', 'LITE_EXECUTOR', 'READY', '[0]', 'TASK_RUNNING', '', '2020-05-07 15:24:23');
INSERT INTO `job_status_trace_log` VALUES ('c30e4a34-b955-4a4d-bbc8-2d56271a511e', 'com.ckh.enjoy.web.service.job.SimpleJobDemo', '', 'com.ckh.enjoy.web.service.job.SimpleJobDemo@-@0@-@READY@-@169.254.171.155@-@14112', '169.254.171.155', 'LITE_EXECUTOR', 'READY', '[0]', 'TASK_RUNNING', '', '2020-05-07 15:29:54');
INSERT INTO `job_status_trace_log` VALUES ('c738a66b-8643-4c7e-9407-53678fc65adc', 'com.ckh.enjoy.web.service.job.SimpleJobDemo', '', 'com.ckh.enjoy.web.service.job.SimpleJobDemo@-@0@-@READY@-@169.254.171.155@-@10892', '169.254.171.155', 'LITE_EXECUTOR', 'READY', '[0]', 'TASK_STAGING', 'Job \'com.ckh.enjoy.web.service.job.SimpleJobDemo\' execute begin.', '2020-05-07 15:24:20');
INSERT INTO `job_status_trace_log` VALUES ('c95e6102-5777-472a-927b-02227136e7c4', 'com.ckh.enjoy.web.service.job.SimpleJobDemo', '', 'com.ckh.enjoy.web.service.job.SimpleJobDemo@-@0@-@READY@-@169.254.171.155@-@14112', '169.254.171.155', 'LITE_EXECUTOR', 'READY', '[0]', 'TASK_FINISHED', '', '2020-05-07 15:29:59');
INSERT INTO `job_status_trace_log` VALUES ('ca169278-008f-4658-a1f2-a7772ac5feaa', 'com.ckh.enjoy.web.service.job.SimpleJobDemo', '', 'com.ckh.enjoy.web.service.job.SimpleJobDemo@-@0@-@READY@-@169.254.171.155@-@14112', '169.254.171.155', 'LITE_EXECUTOR', 'READY', '[0]', 'TASK_RUNNING', '', '2020-05-07 15:29:57');
INSERT INTO `job_status_trace_log` VALUES ('cb24d6bb-5d97-4513-90ff-b5b729c04650', 'com.ckh.enjoy.web.service.job.SimpleJobDemo', '', 'com.ckh.enjoy.web.service.job.SimpleJobDemo@-@0@-@READY@-@169.254.171.155@-@14112', '169.254.171.155', 'LITE_EXECUTOR', 'READY', '[0]', 'TASK_RUNNING', '', '2020-05-07 15:29:55');
INSERT INTO `job_status_trace_log` VALUES ('d0a7d18a-877f-4452-a659-c7a6972b4373', 'com.ckh.enjoy.web.service.job.SimpleJobDemo', '', 'com.ckh.enjoy.web.service.job.SimpleJobDemo@-@0@-@READY@-@169.254.171.155@-@14112', '169.254.171.155', 'LITE_EXECUTOR', 'READY', '[0]', 'TASK_FINISHED', '', '2020-05-07 15:29:56');
INSERT INTO `job_status_trace_log` VALUES ('dcf56e63-452c-4197-ab2e-0e70c6671006', 'com.ckh.enjoy.web.service.job.SimpleJobDemo', '', 'com.ckh.enjoy.web.service.job.SimpleJobDemo@-@0@-@READY@-@169.254.171.155@-@10892', '169.254.171.155', 'LITE_EXECUTOR', 'READY', '[0]', 'TASK_FINISHED', '', '2020-05-07 15:24:23');
INSERT INTO `job_status_trace_log` VALUES ('ddac8d62-7db1-445c-9967-605aed5758ad', 'com.ckh.enjoy.web.service.job.SimpleJobDemo', '', 'com.ckh.enjoy.web.service.job.SimpleJobDemo@-@0@-@READY@-@169.254.171.155@-@10892', '169.254.171.155', 'LITE_EXECUTOR', 'READY', '[0]', 'TASK_STAGING', 'Job \'com.ckh.enjoy.web.service.job.SimpleJobDemo\' execute begin.', '2020-05-07 15:24:21');
INSERT INTO `job_status_trace_log` VALUES ('e00f6a26-edbe-4440-9b93-102eebe24de5', 'com.ckh.enjoy.web.service.job.SimpleJobDemo', '', 'com.ckh.enjoy.web.service.job.SimpleJobDemo@-@0,1@-@READY@-@169.254.171.155@-@53016', '169.254.171.155', 'LITE_EXECUTOR', 'READY', '[0, 1]', 'TASK_STAGING', 'Job \'com.ckh.enjoy.web.service.job.SimpleJobDemo\' execute begin.', '2020-05-07 15:09:54');
INSERT INTO `job_status_trace_log` VALUES ('e6eedf28-d1db-4729-97de-05a851662cab', 'com.ckh.enjoy.web.service.job.SimpleJobDemo', '', 'com.ckh.enjoy.web.service.job.SimpleJobDemo@-@0@-@READY@-@169.254.171.155@-@14112', '169.254.171.155', 'LITE_EXECUTOR', 'READY', '[0]', 'TASK_STAGING', 'Job \'com.ckh.enjoy.web.service.job.SimpleJobDemo\' execute begin.', '2020-05-07 15:29:58');
INSERT INTO `job_status_trace_log` VALUES ('e9c3c6cd-7e40-428b-a820-e026dba62663', 'com.ckh.enjoy.web.service.job.SimpleJobDemo', '', 'com.ckh.enjoy.web.service.job.SimpleJobDemo@-@0@-@READY@-@169.254.171.155@-@14112', '169.254.171.155', 'LITE_EXECUTOR', 'READY', '[0]', 'TASK_STAGING', 'Job \'com.ckh.enjoy.web.service.job.SimpleJobDemo\' execute begin.', '2020-05-07 15:29:57');
INSERT INTO `job_status_trace_log` VALUES ('e9ca7cbb-e845-4ea2-aea7-7d6bdfbb1e43', 'com.ckh.enjoy.web.service.job.SimpleJobDemo', '', 'com.ckh.enjoy.web.service.job.SimpleJobDemo@-@0@-@READY@-@169.254.171.155@-@14112', '169.254.171.155', 'LITE_EXECUTOR', 'READY', '[0]', 'TASK_FINISHED', '', '2020-05-07 15:29:53');
INSERT INTO `job_status_trace_log` VALUES ('ea12f1a1-baf5-4f15-9dea-6c673de6a87a', 'com.ckh.enjoy.web.service.job.SimpleJobDemo', '', 'com.ckh.enjoy.web.service.job.SimpleJobDemo@-@0@-@READY@-@169.254.171.155@-@10892', '169.254.171.155', 'LITE_EXECUTOR', 'READY', '[0]', 'TASK_FINISHED', '', '2020-05-07 15:24:26');
INSERT INTO `job_status_trace_log` VALUES ('f68902fb-908c-4355-8ce5-50cd02331324', 'com.ckh.enjoy.web.service.job.SimpleJobDemo', '', 'com.ckh.enjoy.web.service.job.SimpleJobDemo@-@0@-@READY@-@169.254.171.155@-@14112', '169.254.171.155', 'LITE_EXECUTOR', 'READY', '[0]', 'TASK_RUNNING', '', '2020-05-07 15:29:53');
INSERT INTO `job_status_trace_log` VALUES ('f69fd7e6-0255-45a9-b048-ce2ba28bb4d9', 'com.ckh.enjoy.web.service.job.SimpleJobDemo', '', 'com.ckh.enjoy.web.service.job.SimpleJobDemo@-@0@-@READY@-@169.254.171.155@-@10892', '169.254.171.155', 'LITE_EXECUTOR', 'READY', '[0]', 'TASK_RUNNING', '', '2020-05-07 15:24:28');
INSERT INTO `job_status_trace_log` VALUES ('faeb34d0-36e9-471f-9ffd-5f5033a113ef', 'com.ckh.enjoy.web.service.job.SimpleJobDemo', '', 'com.ckh.enjoy.web.service.job.SimpleJobDemo@-@0@-@READY@-@169.254.171.155@-@10892', '169.254.171.155', 'LITE_EXECUTOR', 'READY', '[0]', 'TASK_STAGING', 'Job \'com.ckh.enjoy.web.service.job.SimpleJobDemo\' execute begin.', '2020-05-07 15:24:19');

-- ----------------------------
-- Event structure for del_job_log_func
-- ----------------------------
DROP EVENT IF EXISTS `del_job_log_func`;
delimiter ;;
CREATE EVENT `del_job_log_func`
ON SCHEDULE
EVERY '1' DAY STARTS '2019-08-07 00:00:00'
DO delete from JOB_EXECUTION_LOG where complete_time< DATE_SUB(CURDATE(), INTERVAL 1 DAY)
;;
delimiter ;

-- ----------------------------
-- Event structure for del_job_trace_log_func
-- ----------------------------
DROP EVENT IF EXISTS `del_job_trace_log_func`;
delimiter ;;
CREATE EVENT `del_job_trace_log_func`
ON SCHEDULE
EVERY '1' DAY STARTS '2019-08-07 00:00:00'
DO delete from JOB_STATUS_TRACE_LOG where creation_time< DATE_SUB(CURDATE(), INTERVAL 1 DAY)
;;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;
