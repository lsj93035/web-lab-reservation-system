-- =============================================
-- 实验室预约管理系统 数据库脚本
-- 基于若依(RuoYi)框架
-- =============================================

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- 1. 实验室信息表
-- ----------------------------
DROP TABLE IF EXISTS `sys_lab`;
CREATE TABLE `sys_lab` (
  `lab_id`      int NOT NULL AUTO_INCREMENT COMMENT '实验室编号',
  `lab_name`    varchar(50) DEFAULT NULL COMMENT '实验室名称',
  `building`    varchar(50) DEFAULT NULL COMMENT '所在楼宇',
  `capacity`    int DEFAULT NULL COMMENT '容纳人数',
  `status`      int DEFAULT 0 COMMENT '状态(0正常 1维修)',
  `equipment`   varchar(255) DEFAULT NULL COMMENT '设备清单',
  `create_by`   varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by`   varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark`      varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`lab_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='实验室信息表';

-- ----------------------------
-- 2. 预约信息表
-- ----------------------------
DROP TABLE IF EXISTS `sys_reservation`;
CREATE TABLE `sys_reservation` (
  `reserve_id`   int NOT NULL AUTO_INCREMENT COMMENT '预约编号',
  `user_id`      int NOT NULL COMMENT '预约人编号',
  `lab_id`       int NOT NULL COMMENT '实验室编号',
  `start_date`   datetime NOT NULL COMMENT '开始时间',
  `end_date`     datetime NOT NULL COMMENT '结束时间',
  `audit_status` int DEFAULT 0 COMMENT '审核状态(0待审 1通过 2驳回)',
  `remark`       varchar(255) DEFAULT NULL COMMENT '实验备注',
  `create_by`    varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time`  datetime DEFAULT NULL COMMENT '创建时间',
  `update_by`    varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time`  datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`reserve_id`),
  KEY `idx_lab_date` (`lab_id`, `start_date`, `end_date`),
  KEY `idx_user` (`user_id`),
  KEY `idx_audit_status` (`audit_status`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='预约信息表';

-- ----------------------------
-- 3. 设备报修表
-- ----------------------------
DROP TABLE IF EXISTS `sys_repair`;
CREATE TABLE `sys_repair` (
  `repair_id`     int NOT NULL AUTO_INCREMENT COMMENT '报修单号',
  `lab_id`        int NOT NULL COMMENT '实验室编号',
  `user_id`       int NOT NULL COMMENT '报修人编号',
  `fault_desc`    varchar(255) DEFAULT NULL COMMENT '故障描述',
  `repair_status` int DEFAULT 0 COMMENT '维修状态(0待处理 1维修中 2已修复)',
  `submit_time`   datetime DEFAULT NULL COMMENT '提交时间',
  `create_by`     varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time`   datetime DEFAULT NULL COMMENT '创建时间',
  `update_by`     varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time`   datetime DEFAULT NULL COMMENT '更新时间',
  `remark`        varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`repair_id`),
  KEY `idx_lab` (`lab_id`),
  KEY `idx_user` (`user_id`),
  KEY `idx_status` (`repair_status`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='设备报修表';

-- ----------------------------
-- 4. 教学占用表
-- ----------------------------
DROP TABLE IF EXISTS `sys_teaching_occupancy`;
CREATE TABLE `sys_teaching_occupancy` (
  `occupy_id`   int NOT NULL AUTO_INCREMENT COMMENT '占用编号',
  `teacher_id`  int NOT NULL COMMENT '教师编号',
  `lab_id`      int NOT NULL COMMENT '实验室编号',
  `course_name` varchar(100) DEFAULT NULL COMMENT '课程名称',
  `occupy_date` date NOT NULL COMMENT '占用日期',
  `time_slot`   varchar(50) NOT NULL COMMENT '使用时段(如08:00-10:00)',
  `create_by`   varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by`   varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`occupy_id`),
  KEY `idx_lab_date` (`lab_id`, `occupy_date`),
  KEY `idx_teacher` (`teacher_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='教学占用表';

-- ----------------------------
-- 5. 数据字典 - 实验室状态
-- ----------------------------
INSERT INTO `sys_dict_type` VALUES (100, '实验室状态', 'lab_status', '0', 'admin', NOW(), '', NULL, '实验室状态列表');
INSERT INTO `sys_dict_data` VALUES (100, 1, '正常', '0', 'lab_status', '', 'primary', 'Y', '0', 'admin', NOW(), '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (101, 2, '维修', '1', 'lab_status', '', 'danger', 'N', '0', 'admin', NOW(), '', NULL, '维修状态');

-- ----------------------------
-- 6. 数据字典 - 审核状态
-- ----------------------------
INSERT INTO `sys_dict_type` VALUES (101, '审核状态', 'audit_status', '0', 'admin', NOW(), '', NULL, '预约审核状态列表');
INSERT INTO `sys_dict_data` VALUES (102, 1, '待审核', '0', 'audit_status', '', 'primary', 'Y', '0', 'admin', NOW(), '', NULL, '待审核状态');
INSERT INTO `sys_dict_data` VALUES (103, 2, '已通过', '1', 'audit_status', '', 'success', 'N', '0', 'admin', NOW(), '', NULL, '已通过状态');
INSERT INTO `sys_dict_data` VALUES (104, 3, '已驳回', '2', 'audit_status', '', 'danger', 'N', '0', 'admin', NOW(), '', NULL, '已驳回状态');

-- ----------------------------
-- 7. 数据字典 - 维修状态
-- ----------------------------
INSERT INTO `sys_dict_type` VALUES (102, '维修状态', 'repair_status', '0', 'admin', NOW(), '', NULL, '设备维修状态列表');
INSERT INTO `sys_dict_data` VALUES (105, 1, '待处理', '0', 'repair_status', '', 'primary', 'Y', '0', 'admin', NOW(), '', NULL, '待处理状态');
INSERT INTO `sys_dict_data` VALUES (106, 2, '维修中', '1', 'repair_status', '', 'warning', 'N', '0', 'admin', NOW(), '', NULL, '维修中状态');
INSERT INTO `sys_dict_data` VALUES (107, 3, '已修复', '2', 'repair_status', '', 'success', 'N', '0', 'admin', NOW(), '', NULL, '已修复状态');

-- ----------------------------
-- 8. 角色配置 - 学生、教师、管理员
-- ----------------------------
INSERT INTO `sys_role` VALUES (200, '学生', 'student', 10, '1', '1', '1', '0', '0', 'admin', NOW(), '', NULL, '学生角色，可查询实验室、预约、报修');
INSERT INTO `sys_role` VALUES (201, '教师', 'teacher', 11, '1', '1', '1', '0', '0', 'admin', NOW(), '', NULL, '教师角色，可审批预约、申请教学占用');
INSERT INTO `sys_role` VALUES (202, '实验室管理员', 'lab_admin', 12, '1', '1', '1', '0', '0', 'admin', NOW(), '', NULL, '实验室管理员，拥有全部实验室管理权限');

-- ----------------------------
-- 9. 菜单配置 - 实验室管理模块
-- ----------------------------

-- 删除旧的实验室菜单（如果存在）
DELETE FROM `sys_role_menu` WHERE `menu_id` >= 2000 AND `menu_id` < 3000;
DELETE FROM `sys_menu` WHERE `menu_id` >= 2000 AND `menu_id` < 3000;

-- 顶级目录：实验室管理
INSERT INTO `sys_menu` VALUES (2000, '实验室管理', 0, 4, 'lab', NULL, '', 1, 0, 'M', '0', '0', '', 'server', 'admin', NOW(), '', NULL, '实验室管理目录');

-- 实验室信息管理
INSERT INTO `sys_menu` VALUES (2001, '实验室信息管理', 2000, 1, 'lab', 'lab-management/lab/index', '', 1, 0, 'C', '0', '0', 'lab:lab:list', 'edit', 'admin', NOW(), '', NULL, '实验室信息管理菜单');
INSERT INTO `sys_menu` VALUES (2003, '新增实验室', 2001, 1, '', '', '', 1, 0, 'F', '0', '0', 'lab:lab:add', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO `sys_menu` VALUES (2004, '修改实验室', 2001, 2, '', '', '', 1, 0, 'F', '0', '0', 'lab:lab:edit', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO `sys_menu` VALUES (2005, '查询实验室', 2001, 3, '', '', '', 1, 0, 'F', '0', '0', 'lab:lab:query', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO `sys_menu` VALUES (2006, '删除实验室', 2001, 4, '', '', '', 1, 0, 'F', '0', '0', 'lab:lab:remove', '#', 'admin', NOW(), '', NULL, '');

-- 在线预约
INSERT INTO `sys_menu` VALUES (2010, '在线预约', 2000, 2, 'reservation', 'lab-management/reservation/index', '', 1, 0, 'C', '0', '0', 'lab:reservation:list', 'time-range', 'admin', NOW(), '', NULL, '在线预约菜单');
INSERT INTO `sys_menu` VALUES (2011, '查看预约详情', 2010, 1, '', '', '', 1, 0, 'F', '0', '0', 'lab:reservation:query', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO `sys_menu` VALUES (2012, '提交预约', 2010, 2, '', '', '', 1, 0, 'F', '0', '0', 'lab:reservation:add', '#', 'admin', NOW(), '', NULL, '');

-- 我的预约
INSERT INTO `sys_menu` VALUES (2020, '我的预约', 2000, 3, 'myreservation', 'lab-management/myreservation/index', '', 1, 0, 'C', '0', '0', 'lab:myreservation:list', 'star', 'admin', NOW(), '', NULL, '我的预约菜单');
INSERT INTO `sys_menu` VALUES (2021, '查看我的预约', 2020, 1, '', '', '', 1, 0, 'F', '0', '0', 'lab:myreservation:query', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO `sys_menu` VALUES (2022, '撤销预约', 2020, 2, '', '', '', 1, 0, 'F', '0', '0', 'lab:myreservation:cancel', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO `sys_menu` VALUES (2023, '导出Excel', 2020, 3, '', '', '', 1, 0, 'F', '0', '0', 'lab:myreservation:export', '#', 'admin', NOW(), '', NULL, '');

-- 预约审批
INSERT INTO `sys_menu` VALUES (2030, '预约审批', 2000, 4, 'audit', 'lab-management/audit/index', '', 1, 0, 'C', '0', '0', 'lab:audit:list', 'peoples', 'admin', NOW(), '', NULL, '预约审批菜单');
INSERT INTO `sys_menu` VALUES (2031, '审批操作', 2030, 1, '', '', '', 1, 0, 'F', '0', '0', 'lab:audit:approve', '#', 'admin', NOW(), '', NULL, '');

-- 教学占用
INSERT INTO `sys_menu` VALUES (2040, '教学占用', 2000, 5, 'teaching', 'lab-management/teaching/index', '', 1, 0, 'C', '0', '0', 'lab:teaching:list', 'education', 'admin', NOW(), '', NULL, '教学占用菜单');
INSERT INTO `sys_menu` VALUES (2041, '新增教学占用', 2040, 1, '', '', '', 1, 0, 'F', '0', '0', 'lab:teaching:add', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO `sys_menu` VALUES (2042, '删除教学占用', 2040, 2, '', '', '', 1, 0, 'F', '0', '0', 'lab:teaching:remove', '#', 'admin', NOW(), '', NULL, '');

-- 设备报修
INSERT INTO `sys_menu` VALUES (2050, '设备报修', 2000, 6, 'repair', 'lab-management/repair/index', '', 1, 0, 'C', '0', '0', 'lab:repair:list', 'log', 'admin', NOW(), '', NULL, '设备报修菜单');
INSERT INTO `sys_menu` VALUES (2051, '提交报修', 2050, 1, '', '', '', 1, 0, 'F', '0', '0', 'lab:repair:add', '#', 'admin', NOW(), '', NULL, '');

-- 我的报修
INSERT INTO `sys_menu` VALUES (2060, '我的报修', 2000, 7, 'myrepair', 'lab-management/myrepair/index', '', 1, 0, 'C', '0', '0', 'lab:myrepair:list', 'people', 'admin', NOW(), '', NULL, '我的报修菜单');
INSERT INTO `sys_menu` VALUES (2061, '导出Excel', 2060, 1, '', '', '', 1, 0, 'F', '0', '0', 'lab:myrepair:export', '#', 'admin', NOW(), '', NULL, '');

-- 维修管理
INSERT INTO `sys_menu` VALUES (2070, '维修管理', 2000, 8, 'manage-repair', 'lab-management/manage-repair/index', '', 1, 0, 'C', '0', '0', 'lab:managerepair:list', 'tool', 'admin', NOW(), '', NULL, '维修管理菜单');
INSERT INTO `sys_menu` VALUES (2071, '更新维修状态', 2070, 1, '', '', '', 1, 0, 'F', '0', '0', 'lab:managerepair:edit', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO `sys_menu` VALUES (2072, '导出Excel', 2070, 2, '', '', '', 1, 0, 'F', '0', '0', 'lab:managerepair:export', '#', 'admin', NOW(), '', NULL, '');

-- 预约记录
INSERT INTO `sys_menu` VALUES (2080, '预约记录', 2000, 9, 'aboutme', 'lab-management/aboutme/index', '', 1, 0, 'C', '0', '0', 'lab:aboutme:list', 'rate', 'admin', NOW(), '', NULL, '预约记录菜单');
INSERT INTO `sys_menu` VALUES (2081, '查看记录详情', 2080, 1, '', '', '', 1, 0, 'F', '0', '0', 'lab:aboutme:query', '#', 'admin', NOW(), '', NULL, '');

-- ----------------------------
-- 10. 角色菜单关联 - 学生
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (200, 2000);
INSERT INTO `sys_role_menu` VALUES (200, 2001);
INSERT INTO `sys_role_menu` VALUES (200, 2005);
INSERT INTO `sys_role_menu` VALUES (200, 2010);
INSERT INTO `sys_role_menu` VALUES (200, 2011);
INSERT INTO `sys_role_menu` VALUES (200, 2012);
INSERT INTO `sys_role_menu` VALUES (200, 2020);
INSERT INTO `sys_role_menu` VALUES (200, 2021);
INSERT INTO `sys_role_menu` VALUES (200, 2022);
INSERT INTO `sys_role_menu` VALUES (200, 2023);
INSERT INTO `sys_role_menu` VALUES (200, 2050);
INSERT INTO `sys_role_menu` VALUES (200, 2051);
INSERT INTO `sys_role_menu` VALUES (200, 2060);
INSERT INTO `sys_role_menu` VALUES (200, 2061);
INSERT INTO `sys_role_menu` VALUES (200, 2080);
INSERT INTO `sys_role_menu` VALUES (200, 2081);

-- ----------------------------
-- 11. 角色菜单关联 - 教师
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (201, 2000);
INSERT INTO `sys_role_menu` VALUES (201, 2001);
INSERT INTO `sys_role_menu` VALUES (201, 2005);
INSERT INTO `sys_role_menu` VALUES (201, 2010);
INSERT INTO `sys_role_menu` VALUES (201, 2011);
INSERT INTO `sys_role_menu` VALUES (201, 2030);
INSERT INTO `sys_role_menu` VALUES (201, 2031);
INSERT INTO `sys_role_menu` VALUES (201, 2040);
INSERT INTO `sys_role_menu` VALUES (201, 2041);
INSERT INTO `sys_role_menu` VALUES (201, 2042);
INSERT INTO `sys_role_menu` VALUES (201, 2050);
INSERT INTO `sys_role_menu` VALUES (201, 2060);
INSERT INTO `sys_role_menu` VALUES (201, 2061);
INSERT INTO `sys_role_menu` VALUES (201, 2070);
INSERT INTO `sys_role_menu` VALUES (201, 2071);
INSERT INTO `sys_role_menu` VALUES (201, 2072);
INSERT INTO `sys_role_menu` VALUES (201, 2080);
INSERT INTO `sys_role_menu` VALUES (201, 2081);

-- ----------------------------
-- 12. 角色菜单关联 - 管理员（全部权限）
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (202, 2000);
INSERT INTO `sys_role_menu` VALUES (202, 2001);
INSERT INTO `sys_role_menu` VALUES (202, 2003);
INSERT INTO `sys_role_menu` VALUES (202, 2004);
INSERT INTO `sys_role_menu` VALUES (202, 2005);
INSERT INTO `sys_role_menu` VALUES (202, 2006);
INSERT INTO `sys_role_menu` VALUES (202, 2010);
INSERT INTO `sys_role_menu` VALUES (202, 2011);
INSERT INTO `sys_role_menu` VALUES (202, 2012);
INSERT INTO `sys_role_menu` VALUES (202, 2020);
INSERT INTO `sys_role_menu` VALUES (202, 2021);
INSERT INTO `sys_role_menu` VALUES (202, 2022);
INSERT INTO `sys_role_menu` VALUES (202, 2023);
INSERT INTO `sys_role_menu` VALUES (202, 2030);
INSERT INTO `sys_role_menu` VALUES (202, 2031);
INSERT INTO `sys_role_menu` VALUES (202, 2040);
INSERT INTO `sys_role_menu` VALUES (202, 2041);
INSERT INTO `sys_role_menu` VALUES (202, 2042);
INSERT INTO `sys_role_menu` VALUES (202, 2050);
INSERT INTO `sys_role_menu` VALUES (202, 2051);
INSERT INTO `sys_role_menu` VALUES (202, 2060);
INSERT INTO `sys_role_menu` VALUES (202, 2061);
INSERT INTO `sys_role_menu` VALUES (202, 2070);
INSERT INTO `sys_role_menu` VALUES (202, 2071);
INSERT INTO `sys_role_menu` VALUES (202, 2072);
INSERT INTO `sys_role_menu` VALUES (202, 2080);
INSERT INTO `sys_role_menu` VALUES (202, 2081);

-- ----------------------------
-- 13. 岗位配置 - 学生、教师
-- ----------------------------
INSERT INTO `sys_post` VALUES (5, 'student', '学生', 5, '0', 'admin', NOW(), '', NULL, '学生岗位');
INSERT INTO `sys_post` VALUES (6, 'teacher', '教师', 6, '0', 'admin', NOW(), '', NULL, '教师岗位');

-- ----------------------------
-- 14. 实验室数据
-- ----------------------------
INSERT INTO `sys_lab` VALUES (1, '实验室1', '教学楼A栋', 40, 0, '电脑40台、投影仪1台、实验仪器若干', 'admin', NOW(), '', NULL, '基础教学实验室');
INSERT INTO `sys_lab` VALUES (2, '实验室2', '教学楼A栋', 35, 0, '电脑35台、显微镜20台、化学实验器材', 'admin', NOW(), '', NULL, '化学分析实验室');
INSERT INTO `sys_lab` VALUES (3, '实验室3', '实验楼B栋', 30, 0, '示波器15台、信号发生器15台、电路板若干', 'admin', NOW(), '', NULL, '电子电路实验室');

-- ----------------------------
-- 15. 预约记录（含待审批记录）
-- ----------------------------
-- 学生(user_id=100)预约实验室1，待审批
INSERT INTO `sys_reservation` VALUES (1, 100, 1, '2026-06-07 08:00:00', '2026-06-07 10:00:00', 0, '数据结构实验课', 'admin', NOW(), '', NULL);
-- 学生(user_id=100)预约实验室2，已通过
INSERT INTO `sys_reservation` VALUES (2, 100, 2, '2026-06-06 14:00:00', '2026-06-06 16:00:00', 1, '化学分析实验', 'admin', NOW(), '', NULL);
-- 学生(user_id=101)预约实验室1，待审批
INSERT INTO `sys_reservation` VALUES (3, 101, 1, '2026-06-08 10:00:00', '2026-06-08 12:00:00', 0, '操作系统实验', 'admin', NOW(), '', NULL);
-- 学生(user_id=101)预约实验室3，待审批
INSERT INTO `sys_reservation` VALUES (4, 101, 3, '2026-06-09 08:00:00', '2026-06-09 10:00:00', 0, '电路分析实验', 'admin', NOW(), '', NULL);
-- 学生(user_id=102)预约实验室2，已驳回
INSERT INTO `sys_reservation` VALUES (5, 102, 2, '2026-06-05 14:00:00', '2026-06-05 16:00:00', 2, '有机化学实验', 'admin', NOW(), '', NULL);
-- 教师(user_id=104)预约实验室1，待审批
INSERT INTO `sys_reservation` VALUES (6, 104, 1, '2026-06-10 08:00:00', '2026-06-10 12:00:00', 0, '编程实训课程', 'admin', NOW(), '', NULL);

-- ----------------------------
-- 16. 设备报修记录
-- ----------------------------
-- 实验室1报修，待处理
INSERT INTO `sys_repair` VALUES (1, 1, 100, '第3排电脑无法开机', 0, NOW(), 'admin', NOW(), '', NULL, NULL);
-- 实验室2报修，维修中
INSERT INTO `sys_repair` VALUES (2, 2, 101, '显微镜镜头模糊', 1, NOW(), 'admin', NOW(), '', NULL, NULL);
-- 实验室3报修，已修复
INSERT INTO `sys_repair` VALUES (3, 3, 102, '示波器显示异常', 2, NOW(), 'admin', NOW(), '', NULL, NULL);
-- 实验室1报修，待处理
INSERT INTO `sys_repair` VALUES (4, 1, 104, '投影仪灯泡需要更换', 0, NOW(), 'admin', NOW(), '', NULL, NULL);
-- 实验室2报修，待处理
INSERT INTO `sys_repair` VALUES (5, 2, 100, '通风橱排风不畅', 0, NOW(), 'admin', NOW(), '', NULL, NULL);

-- ----------------------------
-- 17. 教学占用记录
-- ----------------------------
INSERT INTO `sys_teaching_occupancy` VALUES (1, 104, 1, '数据结构', '2026-06-09', '08:00-10:00', 'admin', NOW(), '', NULL);
INSERT INTO `sys_teaching_occupancy` VALUES (2, 104, 2, '化学分析', '2026-06-10', '14:00-16:00', 'admin', NOW(), '', NULL);

SET FOREIGN_KEY_CHECKS=1;
