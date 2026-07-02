# Web Lab Reservation System

基于 Web 的实验室预约管理系统，毕业设计项目。项目在 RuoYi-Vue
前后端分离框架基础上进行二次开发，围绕实验室预约业务实现用户角色管理、实验室资源管理、
预约申请、审批流转、预约冲突检测和设备报修等功能。

## 项目说明

本项目用于实验室资源预约与审批管理，主要面向学生、教师、管理员三类角色：

- 学生：提交实验室预约申请、查看审批状态、取消预约、提交设备报修。
- 教师：查看预约信息、处理审批相关流程。
- 管理员：维护用户、实验室资源、预约记录、设备信息和系统权限。

## 核心功能

- 用户登录与角色权限控制
- 实验室资源管理
- 实验室预约申请与审批流转
- 预约时间冲突检测
- 预约状态更新与记录查询
- 设备报修与管理
- 后台菜单、角色、用户等基础权限管理

## 技术栈

后端：

- Spring Boot
- Spring Security
- MyBatis
- MySQL
- Redis
- JWT

前端：

- Vue
- Element UI
- Axios

基础框架：

- RuoYi-Vue 3.8.6

## 项目结构

```text
.
├── meeting-system       # 实验室预约业务模块
├── ruoyi-admin          # 后端启动模块
├── ruoyi-common         # 通用工具模块
├── ruoyi-framework      # 框架核心模块
├── ruoyi-generator      # 代码生成模块
├── ruoyi-quartz         # 定时任务模块
├── ruoyi-system         # 系统管理模块
├── ruoyi-ui             # Vue 前端项目
└── sql                  # 数据库初始化脚本
```

## 运行环境

- JDK 8
- Maven 3.x
- MySQL 5.7+ / 8.x
- Redis
- Node.js

## 启动方式

1. 创建数据库并导入 `sql/` 目录下的初始化脚本。
2. 修改 `ruoyi-admin/src/main/resources/application-druid.yml` 中的数据库连接信息。
3. 启动 Redis。
4. 后端启动 `ruoyi-admin` 模块中的 `RuoYiApplication`。
5. 前端进入 `ruoyi-ui` 目录安装依赖并启动：

```bash
npm install
npm run dev
```

## 项目亮点

- 基于实验室预约业务拆分学生、教师、管理员三类角色，明确权限边界。
- 围绕预约申请、审批审核、资源管理、状态更新、异常处理等核心流程进行功能设计。
- 对实验室预约时间段进行冲突检测，减少资源重复占用。
- 支持设备报修与实验室资源维护，提升实验室管理效率。
- 结合毕业设计文档完成需求分析、数据库设计、系统实现和测试验证。

## 说明

本项目基于开源框架 RuoYi-Vue 进行二次开发，主要新增和调整内容为实验室预约管理相关业务模块。
