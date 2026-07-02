<template>
  <div class="app-container home">

    <!-- ==================== 欢迎横幅 ==================== -->
    <div class="welcome-banner">
      <div class="welcome-content">
        <div class="welcome-left">
          <h1 class="welcome-title">{{ greeting }}，{{ userName }}</h1>
          <p class="welcome-subtitle">欢迎回到实验室预约管理系统，祝你今天工作顺利！</p>
          <div class="welcome-date">
            <i class="el-icon-date"></i>
            <span>{{ todayDate }}</span>
            <span class="date-divider">|</span>
            <span>{{ todayWeekday }}</span>
          </div>
        </div>
        <div class="welcome-right">
          <div class="welcome-illustration">
            <i class="el-icon-monitor"></i>
          </div>
        </div>
      </div>
    </div>

    <!-- ==================== 数据统计卡片 ==================== -->
    <div class="section-block">
      <div class="section-title">
        <i class="el-icon-data-analysis"></i>
        <span>数据概览</span>
      </div>

      <!-- Admin 统计卡片 -->
      <el-row :gutter="20" v-if="isAdmin">
        <el-col :xs="12" :sm="12" :md="6" v-for="(item, index) in adminCards" :key="index">
          <div class="stat-card" :class="'stat-card--' + item.theme" @click="$router.push(item.route)">
            <div class="stat-card__icon">
              <i :class="item.icon"></i>
            </div>
            <div class="stat-card__content">
              <div class="stat-card__value">{{ item.value }}</div>
              <div class="stat-card__label">{{ item.label }}</div>
            </div>
            <div class="stat-card__deco"></div>
          </div>
        </el-col>
      </el-row>

      <!-- Teacher 统计卡片 -->
      <el-row :gutter="20" v-if="isTeacher">
        <el-col :xs="12" :sm="12" :md="8" v-for="(item, index) in teacherCards" :key="index">
          <div class="stat-card" :class="'stat-card--' + item.theme" @click="$router.push(item.route)">
            <div class="stat-card__icon">
              <i :class="item.icon"></i>
            </div>
            <div class="stat-card__content">
              <div class="stat-card__value">{{ item.value }}</div>
              <div class="stat-card__label">{{ item.label }}</div>
            </div>
            <div class="stat-card__deco"></div>
          </div>
        </el-col>
      </el-row>

      <!-- Student 统计卡片 -->
      <el-row :gutter="20" v-if="isStudent">
        <el-col :xs="12" :sm="12" :md="6" v-for="(item, index) in studentCards" :key="index">
          <div class="stat-card" :class="'stat-card--' + item.theme" @click="$router.push(item.route)">
            <div class="stat-card__icon">
              <i :class="item.icon"></i>
            </div>
            <div class="stat-card__content">
              <div class="stat-card__value">{{ item.value }}</div>
              <div class="stat-card__label">{{ item.label }}</div>
            </div>
            <div class="stat-card__deco"></div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- ==================== 快捷导航 ==================== -->
    <div class="section-block">
      <div class="section-title">
        <i class="el-icon-s-grid"></i>
        <span>快捷功能</span>
      </div>

      <!-- Admin 快捷入口 -->
      <div class="quick-nav-grid" v-if="isAdmin">
        <div class="quick-nav-item" v-for="(item, index) in adminNavs" :key="index" @click="$router.push(item.route)">
          <div class="quick-nav-item__icon" :style="{ background: item.gradient }">
            <i :class="item.icon"></i>
          </div>
          <div class="quick-nav-item__info">
            <span class="quick-nav-item__name">{{ item.name }}</span>
            <span class="quick-nav-item__desc">{{ item.desc }}</span>
          </div>
          <i class="el-icon-arrow-right quick-nav-item__arrow"></i>
        </div>
      </div>

      <!-- Teacher 快捷入口 -->
      <div class="quick-nav-grid" v-if="isTeacher">
        <div class="quick-nav-item" v-for="(item, index) in teacherNavs" :key="index" @click="$router.push(item.route)">
          <div class="quick-nav-item__icon" :style="{ background: item.gradient }">
            <i :class="item.icon"></i>
          </div>
          <div class="quick-nav-item__info">
            <span class="quick-nav-item__name">{{ item.name }}</span>
            <span class="quick-nav-item__desc">{{ item.desc }}</span>
          </div>
          <i class="el-icon-arrow-right quick-nav-item__arrow"></i>
        </div>
      </div>

      <!-- Student 快捷入口 -->
      <div class="quick-nav-grid" v-if="isStudent">
        <div class="quick-nav-item" v-for="(item, index) in studentNavs" :key="index" @click="$router.push(item.route)">
          <div class="quick-nav-item__icon" :style="{ background: item.gradient }">
            <i :class="item.icon"></i>
          </div>
          <div class="quick-nav-item__info">
            <span class="quick-nav-item__name">{{ item.name }}</span>
            <span class="quick-nav-item__desc">{{ item.desc }}</span>
          </div>
          <i class="el-icon-arrow-right quick-nav-item__arrow"></i>
        </div>
      </div>
    </div>

    <!-- ==================== 最近动态 ==================== -->
    <el-row :gutter="20">
      <el-col :xs="24" :md="12">
        <div class="section-block">
          <div class="section-title">
            <i class="el-icon-time"></i>
            <span>系统公告</span>
          </div>
          <div class="notice-list">
            <div class="notice-item" v-for="(item, index) in notices" :key="index">
              <el-tag :type="item.tagType" size="mini" effect="dark">{{ item.tag }}</el-tag>
              <span class="notice-item__text">{{ item.text }}</span>
              <span class="notice-item__time">{{ item.time }}</span>
            </div>
            <div class="notice-empty" v-if="notices.length === 0">
              <i class="el-icon-bell"></i>
              <span>暂无公告</span>
            </div>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :md="12">
        <div class="section-block">
          <div class="section-title">
            <i class="el-icon-chat-dot-round"></i>
            <span>使用提示</span>
          </div>
          <div class="tips-list">
            <div class="tip-item" v-for="(item, index) in tips" :key="index">
              <div class="tip-item__icon" :style="{ background: item.gradient }">
                <i :class="item.icon"></i>
              </div>
              <div class="tip-item__content">
                <span class="tip-item__title">{{ item.title }}</span>
                <span class="tip-item__desc">{{ item.desc }}</span>
              </div>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>

  </div>
</template>

<script>
import { checkRole } from "@/utils/permission";
import { getStatData } from "@/api/index";

export default {
  name: "Index",
  data() {
    return {
      statData: {
        labTotal: 0,
        todayReservations: 0,
        pendingAudit: 0,
        pendingRepair: 0,
        myReservations: 0,
        myRepairs: 0,
        availableLabs: 0
      }
    };
  },
  computed: {
    isAdmin() {
      return checkRole(["admin"]);
    },
    isTeacher() {
      return checkRole(["teacher"]);
    },
    isStudent() {
      return checkRole(["student"]);
    },
    // 用户名
    userName() {
      return this.$store.state.user.name || "管理员";
    },
    // 时间问候语
    greeting() {
      const hour = new Date().getHours();
      if (hour < 9) return "早上好";
      if (hour < 12) return "上午好";
      if (hour < 14) return "中午好";
      if (hour < 18) return "下午好";
      return "晚上好";
    },
    // 今日日期
    todayDate() {
      const d = new Date();
      return `${d.getFullYear()}年${d.getMonth() + 1}月${d.getDate()}日`;
    },
    // 星期
    todayWeekday() {
      const days = ["星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"];
      return days[new Date().getDay()];
    },
    // Admin 统计卡片数据
    adminCards() {
      return [
        { label: "实验室总数", value: this.statData.labTotal, icon: "el-icon-office-building", theme: "blue", route: "/lab/lab" },
        { label: "今日预约数", value: this.statData.todayReservations, icon: "el-icon-date", theme: "green", route: "/lab/reservation" },
        { label: "待审批预约", value: this.statData.pendingAudit, icon: "el-icon-s-check", theme: "orange", route: "/lab/audit" },
        { label: "待处理报修", value: this.statData.pendingRepair, icon: "el-icon-warning-outline", theme: "red", route: "/lab/manage-repair" }
      ];
    },
    // Teacher 统计卡片数据
    teacherCards() {
      return [
        { label: "预约管理审批", value: this.statData.pendingAudit, icon: "el-icon-s-check", theme: "orange", route: "/lab/audit" },
        { label: "我的预约数", value: this.statData.myReservations, icon: "el-icon-time", theme: "blue", route: "/lab/teaching" },
        { label: "设备维修追踪", value: this.statData.pendingRepair, icon: "el-icon-s-tools", theme: "red", route: "/lab/manage-repair" }
      ];
    },
    // Student 统计卡片数据
    studentCards() {
      return [
        { label: "实验室查询", value: this.statData.labTotal, icon: "el-icon-office-building", theme: "blue", route: "/lab/reservation" },
        { label: "我的预约", value: this.statData.myReservations, icon: "el-icon-date", theme: "green", route: "/lab/reservation" },
        { label: "预约记录管理", value: this.statData.myReservations, icon: "el-icon-document", theme: "orange", route: "/lab/myreservation" },
        { label: "设备故障报修", value: this.statData.myRepairs, icon: "el-icon-warning-outline", theme: "red", route: "/lab/repair" }
      ];
    },
    // Admin 快捷导航
    adminNavs() {
      return [
        { name: "实验室管理", desc: "管理实验室信息与状态", icon: "el-icon-office-building", gradient: "linear-gradient(135deg, #409eff, #66b1ff)", route: "/lab/lab" },
        { name: "预约管理", desc: "查看与管理所有预约", icon: "el-icon-date", gradient: "linear-gradient(135deg, #67c23a, #85ce61)", route: "/lab/reservation" },
        { name: "审批管理", desc: "审核待处理的预约申请", icon: "el-icon-s-check", gradient: "linear-gradient(135deg, #e6a23c, #ebb563)", route: "/lab/audit" },
        { name: "教学占用", desc: "管理教学课程占用安排", icon: "el-icon-time", gradient: "linear-gradient(135deg, #909399, #b1b3b8)", route: "/lab/teaching" },
        { name: "维修管理", desc: "处理设备报修工单", icon: "el-icon-s-tools", gradient: "linear-gradient(135deg, #f56c6c, #f89898)", route: "/lab/manage-repair" },
        { name: "用户管理", desc: "管理系统用户与权限", icon: "el-icon-user-solid", gradient: "linear-gradient(135deg, #6366f1, #818cf8)", route: "/system/user" }
      ];
    },
    // Teacher 快捷导航
    teacherNavs() {
      return [
        { name: "预约审批", desc: "审核学生的预约申请", icon: "el-icon-s-check", gradient: "linear-gradient(135deg, #e6a23c, #ebb563)", route: "/lab/audit" },
        { name: "教学占用", desc: "管理教学课程占用安排", icon: "el-icon-time", gradient: "linear-gradient(135deg, #409eff, #66b1ff)", route: "/lab/teaching" },
        { name: "实验室查询", desc: "查询实验室信息与可用时段", icon: "el-icon-office-building", gradient: "linear-gradient(135deg, #67c23a, #85ce61)", route: "/lab/reservation" },
        { name: "我的预约", desc: "查看个人预约记录", icon: "el-icon-document", gradient: "linear-gradient(135deg, #909399, #b1b3b8)", route: "/lab/myreservation" },
        { name: "维修追踪", desc: "跟踪设备维修进度", icon: "el-icon-s-tools", gradient: "linear-gradient(135deg, #f56c6c, #f89898)", route: "/lab/manage-repair" },
        { name: "个人中心", desc: "查看与编辑个人信息", icon: "el-icon-user", gradient: "linear-gradient(135deg, #6366f1, #818cf8)", route: "/user/profile" }
      ];
    },
    // Student 快捷导航
    studentNavs() {
      return [
        { name: "查询实验室", desc: "浏览可用实验室信息", icon: "el-icon-search", gradient: "linear-gradient(135deg, #409eff, #66b1ff)", route: "/lab/reservation" },
        { name: "在线预约", desc: "预约实验室使用时段", icon: "el-icon-plus", gradient: "linear-gradient(135deg, #67c23a, #85ce61)", route: "/lab/reservation" },
        { name: "预约记录", desc: "查看历史预约记录", icon: "el-icon-document", gradient: "linear-gradient(135deg, #e6a23c, #ebb563)", route: "/lab/myreservation" },
        { name: "设备报修", desc: "提交设备故障报修", icon: "el-icon-warning-outline", gradient: "linear-gradient(135deg, #f56c6c, #f89898)", route: "/lab/repair" },
        { name: "报修记录", desc: "查看报修处理进度", icon: "el-icon-s-tools", gradient: "linear-gradient(135deg, #909399, #b1b3b8)", route: "/lab/myrepair" },
        { name: "个人中心", desc: "查看与编辑个人信息", icon: "el-icon-user", gradient: "linear-gradient(135deg, #6366f1, #818cf8)", route: "/user/profile" }
      ];
    },
    // 系统公告
    notices() {
      return [
        { tag: "通知", tagType: "primary", text: "系统已升级至最新版本，新增设备报修功能", time: "今天" },
        { tag: "提醒", tagType: "warning", text: "请各实验室管理员及时处理待审批预约", time: "昨天" },
        { tag: "公告", tagType: "success", text: "新学期实验室预约通道已全面开放", time: "3天前" }
      ];
    },
    // 使用提示
    tips() {
      return [
        { title: "快速预约", desc: "在「实验室查询」中可直接查看空闲时段并预约", icon: "el-icon-date", gradient: "linear-gradient(135deg, #409eff, #66b1ff)" },
        { title: "报修流程", desc: "发现设备故障请及时提交报修，管理员会尽快处理", icon: "el-icon-s-tools", gradient: "linear-gradient(135deg, #f56c6c, #f89898)" },
        { title: "审批通知", desc: "预约审批结果将通过系统消息通知，请留意查看", icon: "el-icon-bell", gradient: "linear-gradient(135deg, #e6a23c, #ebb563)" },
        { title: "个人资料", desc: "请确保个人信息中的联系方式准确无误", icon: "el-icon-user", gradient: "linear-gradient(135deg, #6366f1, #818cf8)" }
      ];
    }
  },
  created() {
    this.loadStatData();
  },
  methods: {
    loadStatData() {
      getStatData().then(response => {
        if (response.code === 200) {
          this.statData = response.data;
        }
      });
    }
  }
};
</script>

<style scoped>
/* ==================== 基础 ==================== */
.home {
  background: #f0f2f5;
  min-height: calc(100vh - 84px);
  padding: 20px;
  animation: fadeIn 0.4s ease;
}
@keyframes fadeIn {
  from { opacity: 0; transform: translateY(8px); }
  to { opacity: 1; transform: translateY(0); }
}

/* ==================== 欢迎横幅 ==================== */
.welcome-banner {
  background: linear-gradient(135deg, #4f46e5 0%, #7c3aed 50%, #6366f1 100%);
  border-radius: 16px;
  padding: 32px 40px;
  position: relative;
  overflow: hidden;
  box-shadow: 0 8px 32px rgba(79, 70, 229, 0.25);
}
.welcome-banner::before {
  content: "";
  position: absolute;
  top: -40%;
  right: -10%;
  width: 400px;
  height: 400px;
  background: radial-gradient(circle, rgba(255,255,255,0.1) 0%, transparent 70%);
  border-radius: 50%;
}
.welcome-banner::after {
  content: "";
  position: absolute;
  bottom: -30%;
  right: 10%;
  width: 250px;
  height: 250px;
  background: radial-gradient(circle, rgba(255,255,255,0.08) 0%, transparent 70%);
  border-radius: 50%;
}
.welcome-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  position: relative;
  z-index: 1;
}
.welcome-left {
  flex: 1;
}
.welcome-title {
  margin: 0 0 8px 0;
  font-size: 26px;
  font-weight: 700;
  color: #ffffff;
  letter-spacing: 0.5px;
}
.welcome-subtitle {
  margin: 0 0 16px 0;
  font-size: 14px;
  color: rgba(255, 255, 255, 0.85);
}
.welcome-date {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  font-size: 13px;
  color: rgba(255, 255, 255, 0.7);
  background: rgba(255, 255, 255, 0.12);
  padding: 6px 16px;
  border-radius: 20px;
  backdrop-filter: blur(4px);
}
.date-divider {
  opacity: 0.5;
}
.welcome-right {
  flex-shrink: 0;
  margin-left: 40px;
}
.welcome-illustration {
  width: 100px;
  height: 100px;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  backdrop-filter: blur(4px);
  animation: float 3s ease-in-out infinite;
}
.welcome-illustration i {
  font-size: 48px;
  color: rgba(255, 255, 255, 0.9);
}
@keyframes float {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-8px); }
}

/* ==================== 区块通用 ==================== */
.section-block {
  background: #ffffff;
  border-radius: 16px;
  padding: 24px;
  margin-top: 20px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.04), 0 1px 2px rgba(0, 0, 0, 0.06);
}
.section-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 16px;
  font-weight: 600;
  color: #1e293b;
  margin-bottom: 20px;
}
.section-title i {
  font-size: 20px;
  color: #6366f1;
}

/* ==================== 统计卡片 ==================== */
.stat-card {
  background: #ffffff;
  border-radius: 14px;
  padding: 22px;
  display: flex;
  align-items: center;
  gap: 18px;
  cursor: pointer;
  transition: all 0.25s ease;
  position: relative;
  overflow: hidden;
  border: 1px solid #f1f5f9;
  margin-bottom: 16px;
}
.stat-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 24px rgba(0, 0, 0, 0.08);
  border-color: transparent;
}
.stat-card__icon {
  width: 52px;
  height: 52px;
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  font-size: 24px;
  color: #ffffff;
  transition: transform 0.25s ease;
}
.stat-card:hover .stat-card__icon {
  transform: scale(1.08);
}
.stat-card__content {
  flex: 1;
  min-width: 0;
}
.stat-card__value {
  font-size: 28px;
  font-weight: 700;
  color: #1e293b;
  line-height: 1.2;
  margin-bottom: 4px;
  font-variant-numeric: tabular-nums;
}
.stat-card__label {
  font-size: 13px;
  color: #94a3b8;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.stat-card__deco {
  position: absolute;
  width: 100px;
  height: 100px;
  border-radius: 50%;
  right: -20px;
  bottom: -20px;
  opacity: 0.06;
  transition: opacity 0.25s ease;
}
.stat-card:hover .stat-card__deco {
  opacity: 0.1;
}

/* 主题色 */
.stat-card--blue .stat-card__icon { background: linear-gradient(135deg, #4f46e5, #6366f1); box-shadow: 0 4px 14px rgba(99, 102, 241, 0.35); }
.stat-card--blue .stat-card__deco { background: #6366f1; }
.stat-card--blue:hover { border-color: rgba(99, 102, 241, 0.2); }

.stat-card--green .stat-card__icon { background: linear-gradient(135deg, #059669, #10b981); box-shadow: 0 4px 14px rgba(16, 185, 129, 0.35); }
.stat-card--green .stat-card__deco { background: #10b981; }
.stat-card--green:hover { border-color: rgba(16, 185, 129, 0.2); }

.stat-card--orange .stat-card__icon { background: linear-gradient(135deg, #d97706, #f59e0b); box-shadow: 0 4px 14px rgba(245, 158, 11, 0.35); }
.stat-card--orange .stat-card__deco { background: #f59e0b; }
.stat-card--orange:hover { border-color: rgba(245, 158, 11, 0.2); }

.stat-card--red .stat-card__icon { background: linear-gradient(135deg, #dc2626, #ef4444); box-shadow: 0 4px 14px rgba(239, 68, 68, 0.35); }
.stat-card--red .stat-card__deco { background: #ef4444; }
.stat-card--red:hover { border-color: rgba(239, 68, 68, 0.2); }

/* ==================== 快捷导航 ==================== */
.quick-nav-grid {
  display: flex;
  flex-direction: column;
  gap: 8px;
}
.quick-nav-item {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 16px 20px;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.2s ease;
  border: 1px solid transparent;
}
.quick-nav-item:hover {
  background: #f8fafc;
  border-color: #e2e8f0;
}
.quick-nav-item__icon {
  width: 46px;
  height: 46px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  font-size: 22px;
  color: #ffffff;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}
.quick-nav-item:hover .quick-nav-item__icon {
  transform: scale(1.06);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.15);
}
.quick-nav-item__info {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
  gap: 2px;
}
.quick-nav-item__name {
  font-size: 14px;
  font-weight: 600;
  color: #1e293b;
}
.quick-nav-item__desc {
  font-size: 12px;
  color: #94a3b8;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.quick-nav-item__arrow {
  font-size: 14px;
  color: #cbd5e1;
  transition: transform 0.2s ease, color 0.2s ease;
}
.quick-nav-item:hover .quick-nav-item__arrow {
  transform: translateX(4px);
  color: #6366f1;
}

/* ==================== 公告列表 ==================== */
.notice-list {
  display: flex;
  flex-direction: column;
  gap: 0;
}
.notice-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 14px 0;
  border-bottom: 1px solid #f1f5f9;
  transition: background 0.15s ease;
}
.notice-item:last-child {
  border-bottom: none;
}
.notice-item:hover {
  background: #f8fafc;
  margin: 0 -12px;
  padding-left: 12px;
  padding-right: 12px;
  border-radius: 8px;
}
.notice-item__text {
  flex: 1;
  font-size: 13px;
  color: #475569;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.notice-item__time {
  font-size: 12px;
  color: #94a3b8;
  flex-shrink: 0;
}
.notice-empty {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 32px 0;
  color: #cbd5e1;
  gap: 8px;
}
.notice-empty i {
  font-size: 32px;
}
.notice-empty span {
  font-size: 13px;
}

/* ==================== 使用提示 ==================== */
.tips-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}
.tip-item {
  display: flex;
  align-items: center;
  gap: 14px;
  padding: 12px 16px;
  border-radius: 10px;
  background: #f8fafc;
  transition: all 0.2s ease;
}
.tip-item:hover {
  background: #f1f5f9;
  transform: translateX(4px);
}
.tip-item__icon {
  width: 38px;
  height: 38px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  font-size: 18px;
  color: #ffffff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}
.tip-item__content {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 2px;
}
.tip-item__title {
  font-size: 13px;
  font-weight: 600;
  color: #1e293b;
}
.tip-item__desc {
  font-size: 12px;
  color: #94a3b8;
}

/* ==================== 响应式 ==================== */
@media (max-width: 768px) {
  .home {
    padding: 12px;
  }
  .welcome-banner {
    padding: 24px 20px;
    border-radius: 12px;
  }
  .welcome-title {
    font-size: 20px;
  }
  .welcome-right {
    display: none;
  }
  .section-block {
    padding: 18px;
    border-radius: 12px;
    margin-top: 14px;
  }
  .stat-card {
    padding: 16px;
    gap: 14px;
    margin-bottom: 12px;
  }
  .stat-card__icon {
    width: 44px;
    height: 44px;
    font-size: 20px;
  }
  .stat-card__value {
    font-size: 22px;
  }
  .quick-nav-item {
    padding: 12px 14px;
  }
  .quick-nav-item__icon {
    width: 40px;
    height: 40px;
    font-size: 18px;
  }
}
</style>
