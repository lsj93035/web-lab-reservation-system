<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="reservationList">
      <el-table-column label="预约编号" align="center" prop="reserveId" />
      <el-table-column label="实验室名称" align="center" prop="labName" />
      <el-table-column label="所在楼宇" align="center" prop="building" />
      <el-table-column label="开始时间" align="center" prop="startDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.startDate) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="结束时间" align="center" prop="endDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.endDate) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="审核状态" align="center" prop="auditStatus">
        <template slot-scope="scope">
          <el-tag :type="scope.row.auditStatus == 0 ? 'primary' : scope.row.auditStatus == 1 ? 'success' : 'danger'">
            {{ scope.row.auditStatus == 0 ? '待审核' : scope.row.auditStatus == 1 ? '已通过' : '已驳回' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="实验备注" align="center" prop="remark" :show-overflow-tooltip="true" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleView(scope.row)" v-hasPermi="['lab:aboutme:query']">查看</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 查看详情对话框 -->
    <el-dialog title="预约详情" :visible.sync="viewDialogOpen" width="500px" append-to-body>
      <el-form label-width="100px">
        <el-form-item label="预约编号">{{ viewForm.reserveId }}</el-form-item>
        <el-form-item label="实验室名称">{{ viewForm.labName }}</el-form-item>
        <el-form-item label="所在楼宇">{{ viewForm.building }}</el-form-item>
        <el-form-item label="开始时间">{{ parseTime(viewForm.startDate) }}</el-form-item>
        <el-form-item label="结束时间">{{ parseTime(viewForm.endDate) }}</el-form-item>
        <el-form-item label="审核状态">
          <el-tag :type="viewForm.auditStatus == 0 ? 'primary' : viewForm.auditStatus == 1 ? 'success' : 'danger'">
            {{ viewForm.auditStatus == 0 ? '待审核' : viewForm.auditStatus == 1 ? '已通过' : '已驳回' }}
          </el-tag>
        </el-form-item>
        <el-form-item label="实验备注">{{ viewForm.remark || '无' }}</el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import { listAboutMe } from "@/api/lab-management/about_me";

export default {
  name: "AboutMe",
  data() {
    return {
      loading: true,
      showSearch: true,
      total: 0,
      reservationList: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
      },
      viewDialogOpen: false,
      viewForm: {},
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询列表 */
    getList() {
      this.loading = true;
      listAboutMe(this.queryParams).then((response) => {
        this.reservationList = response.rows;
        this.total = response.total;
      }).catch(() => {
        this.reservationList = [];
        this.total = 0;
      }).finally(() => {
        this.loading = false;
      });
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 查看详情 */
    handleView(row) {
      this.viewForm = row;
      this.viewDialogOpen = true;
    },
  },
};
</script>
