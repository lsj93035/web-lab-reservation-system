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
      <el-table-column label="预约人" align="center" prop="userName" />
      <el-table-column label="实验备注" align="center" prop="remark" :show-overflow-tooltip="true" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="200">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-check" @click="handleAudit(scope.row, 1)" v-hasPermi="['lab:audit:approve']" style="color: #67C23A;">通过</el-button>
          <el-button size="mini" type="text" icon="el-icon-close" @click="handleAudit(scope.row, 2)" v-hasPermi="['lab:audit:approve']" style="color: #F56C6C;">驳回</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />
  </div>
</template>

<script>
import { listPendingReservation, auditReservation } from "@/api/lab-management/audit";

export default {
  name: "Audit",
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
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询待审核列表 */
    getList() {
      this.loading = true;
      listPendingReservation(this.queryParams).then((response) => {
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
    /** 审批操作 */
    handleAudit(row, status) {
      const statusText = status === 1 ? '通过' : '驳回';
      this.$modal.confirm('确认' + statusText + '该预约申请？').then(function () {
        return auditReservation({ reserveId: row.reserveId, auditStatus: status });
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("审核" + statusText + "成功");
      }).catch(() => {});
    },
  },
};
</script>
