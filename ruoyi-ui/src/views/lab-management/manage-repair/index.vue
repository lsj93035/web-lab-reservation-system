<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="实验室名称" prop="labName">
        <el-input v-model="queryParams.labName" placeholder="请输入实验室名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="故障描述" prop="faultDesc">
        <el-input v-model="queryParams.faultDesc" placeholder="请输入故障描述" clearable @keyup.enter.native="handleQuery" />
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
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['lab:managerepair:export']">导出</el-button>
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
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="200">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleEdit(scope.row)" v-hasPermi="['lab:managerepair:edit']">维修管理</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 维修管理对话框 -->
    <el-dialog title="维修管理" :visible.sync="editDialogOpen" width="500px" append-to-body>
      <el-form ref="editForm" :model="editForm" label-width="100px">
        <el-form-item label="报修单号">{{ editForm.repairId }}</el-form-item>
        <el-form-item label="实验室名称">{{ editForm.labName }}</el-form-item>
        <el-form-item label="故障描述">{{ editForm.faultDesc }}</el-form-item>
        <el-form-item label="维修状态">
          <el-radio-group v-model="editForm.newRepairStatus">
            <el-radio :label="1">维修中</el-radio>
            <el-radio :label="2">已修复</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="实验室状态" v-if="editForm.newRepairStatus == 2">
          <el-radio-group v-model="editForm.labStatus">
            <el-radio :label="0">正常</el-radio>
            <el-radio :label="1">维修</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="完成留言" v-if="editForm.newRepairStatus == 2">
          <el-input v-model="editForm.remark" type="textarea" placeholder="请输入完成留言" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitEdit">确 定</el-button>
        <el-button @click="editDialogOpen = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listManageRepair, updateRepairStatus } from "@/api/lab-management/manage_repair";

export default {
  name: "ManageRepair",
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
        faultDesc: null,
        repairStatus: null,
      },
      editDialogOpen: false,
      editForm: {
        repairId: null,
        labName: null,
        faultDesc: null,
        newRepairStatus: null,
        labStatus: null,
        remark: null,
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
      listManageRepair(this.queryParams).then((response) => {
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
    /** 维修管理 */
    handleEdit(row) {
      this.editForm = {
        repairId: row.repairId,
        labName: row.labName,
        faultDesc: row.faultDesc,
        newRepairStatus: row.repairStatus == 0 ? 1 : row.repairStatus,
        labStatus: 0,
        remark: null,
      };
      this.editDialogOpen = true;
    },
    /** 提交修改 */
    submitEdit() {
      const data = {
        repairId: this.editForm.repairId,
        newRepairStatus: this.editForm.newRepairStatus,
        labStatus: this.editForm.newRepairStatus == 2 ? this.editForm.labStatus : null,
        remark: this.editForm.remark,
      };
      updateRepairStatus(data).then((response) => {
        this.$modal.msgSuccess("更新成功");
        this.editDialogOpen = false;
        this.getList();
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download("manage-repair/export", { ...this.queryParams }, `managerepair_${new Date().getTime()}.xlsx`);
    },
  },
};
</script>
