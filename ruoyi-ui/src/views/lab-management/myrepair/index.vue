<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="实验室名称" prop="labName">
        <el-input v-model="queryParams.labName" placeholder="请输入实验室名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="维修状态" prop="repairStatus">
        <el-select v-model="queryParams.repairStatus" placeholder="请选择维修状态" clearable>
          <el-option v-for="dict in dict.type.repair_status" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['lab:myrepair:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="repairList">
      <el-table-column label="报修单号" align="center" prop="repairId" />
      <el-table-column label="实验室名称" align="center" prop="labName" />
      <el-table-column label="所在楼宇" align="center" prop="building" />
      <el-table-column label="故障描述" align="center" prop="faultDesc" :show-overflow-tooltip="true" />
      <el-table-column label="维修状态" align="center" prop="repairStatus">
        <template slot-scope="scope">
          <el-tag :type="scope.row.repairStatus == 0 ? 'primary' : scope.row.repairStatus == 1 ? 'warning' : 'success'">
            {{ scope.row.repairStatus == 0 ? '待处理' : scope.row.repairStatus == 1 ? '维修中' : '已修复' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="提交时间" align="center" prop="submitTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.submitTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" :show-overflow-tooltip="true" />
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />
  </div>
</template>

<script>
import { listMyRepair } from "@/api/lab-management/repair";

export default {
  name: "MyRepair",
  dicts: ['repair_status'],
  data() {
    return {
      loading: true,
      showSearch: true,
      total: 0,
      repairList: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        labName: null,
        repairStatus: null,
      },
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询列表 */
    getList() {
      this.loading = true;
      listMyRepair(this.queryParams).then((response) => {
        this.repairList = response.rows;
        this.total = response.total;
      }).catch(() => {
        this.repairList = [];
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
    /** 导出按钮操作 */
    handleExport() {
      this.download("repair/export", { ...this.queryParams }, `myrepair_${new Date().getTime()}.xlsx`);
    },
  },
};
</script>
