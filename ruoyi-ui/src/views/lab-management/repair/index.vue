<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="实验室名称" prop="labName">
        <el-input v-model="queryParams.labName" placeholder="请输入实验室名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="所在楼宇" prop="building">
        <el-input v-model="queryParams.building" placeholder="请输入楼宇" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="labList">
      <el-table-column label="实验室编号" align="center" prop="labId" />
      <el-table-column label="实验室名称" align="center" prop="labName" />
      <el-table-column label="所在楼宇" align="center" prop="building" />
      <el-table-column label="容纳人数" align="center" prop="capacity" />
      <el-table-column label="设备清单" align="center" prop="equipment" :show-overflow-tooltip="true" />
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status == 0 ? 'success' : 'danger'">
            {{ scope.row.status == 0 ? '正常' : '维修' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleRepair(scope.row)" v-hasPermi="['lab:repair:add']">进行报修</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 报修对话框 -->
    <el-dialog title="设备报修" :visible.sync="repairDialogOpen" width="500px" append-to-body>
      <el-form ref="repairForm" :model="repairForm" :rules="repairRules" label-width="100px">
        <el-form-item label="实验室名称">
          <el-input v-model="currentLab.labName" disabled />
        </el-form-item>
        <el-form-item label="所在楼宇">
          <el-input v-model="currentLab.building" disabled />
        </el-form-item>
        <el-form-item label="故障描述" prop="faultDesc">
          <el-input v-model="repairForm.faultDesc" type="textarea" placeholder="请描述设备故障情况" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitRepair">确 定</el-button>
        <el-button @click="repairDialogOpen = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listLab } from "@/api/lab-management/lab";
import { addRepair } from "@/api/lab-management/repair";

export default {
  name: "Repair",
  data() {
    return {
      loading: true,
      showSearch: true,
      total: 0,
      labList: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        labName: null,
        building: null,
      },
      // 报修对话框
      repairDialogOpen: false,
      currentLab: {},
      repairForm: {
        labId: null,
        faultDesc: null,
      },
      repairRules: {
        faultDesc: [{ required: true, message: "请输入故障描述", trigger: "blur" }],
      },
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询实验室列表 */
    getList() {
      this.loading = true;
      listLab(this.queryParams).then((response) => {
        this.labList = response.rows;
        this.total = response.total;
      }).catch(() => {
        this.labList = [];
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
    /** 进行报修 */
    handleRepair(row) {
      this.currentLab = row;
      this.repairForm = {
        labId: row.labId,
        faultDesc: null,
      };
      this.repairDialogOpen = true;
    },
    /** 提交报修 */
    submitRepair() {
      this.$refs["repairForm"].validate((valid) => {
        if (valid) {
          addRepair(this.repairForm).then((response) => {
            this.$modal.msgSuccess("报修提交成功");
            this.repairDialogOpen = false;
          });
        }
      });
    },
  },
};
</script>
