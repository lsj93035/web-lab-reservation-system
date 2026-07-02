<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="实验室名称" prop="labName">
        <el-input v-model="queryParams.labName" placeholder="请输入实验室名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="所在楼宇" prop="building">
        <el-input v-model="queryParams.building" placeholder="请输入楼宇" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="审核状态" prop="auditStatus">
        <el-select v-model="queryParams.auditStatus" placeholder="请选择审核状态" clearable>
          <el-option v-for="dict in dict.type.audit_status" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['lab:myreservation:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

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
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleView(scope.row)" v-hasPermi="['lab:myreservation:query']">查看</el-button>
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['lab:myreservation:edit']" v-if="scope.row.auditStatus == 0">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleCancel(scope.row)" v-hasPermi="['lab:myreservation:cancel']" v-if="scope.row.auditStatus == 0">撤销</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 查看预约详情对话框 -->
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

    <!-- 修改预约对话框 -->
    <el-dialog title="修改预约" :visible.sync="editDialogOpen" width="500px" append-to-body>
      <el-form ref="editForm" :model="editForm" :rules="editRules" label-width="100px">
        <el-form-item label="预约时间" prop="bookingTime">
          <el-date-picker v-model="editForm.bookingTime" type="datetimerange" range-separator="至" start-placeholder="开始时间" end-placeholder="结束时间" value-format="yyyy-MM-dd HH:mm:ss" />
        </el-form-item>
        <el-form-item label="实验备注" prop="remark">
          <el-input v-model="editForm.remark" type="textarea" placeholder="请输入实验备注" />
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
import { listMyReservation, getReservation, updateReservation, cancelReservation } from "@/api/lab-management/reservation";

export default {
  name: "MyReservation",
  dicts: ['audit_status'],
  data() {
    return {
      loading: true,
      showSearch: true,
      total: 0,
      reservationList: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        labName: null,
        building: null,
        auditStatus: null,
      },
      // 查看详情
      viewDialogOpen: false,
      viewForm: {},
      // 修改预约
      editDialogOpen: false,
      editForm: {
        reserveId: null,
        labId: null,
        bookingTime: null,
        remark: null,
      },
      editRules: {
        bookingTime: [{ required: true, message: "请选择预约时间", trigger: "blur" }],
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
      listMyReservation(this.queryParams).then((response) => {
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
    /** 修改预约 */
    handleUpdate(row) {
      this.editForm = {
        reserveId: row.reserveId,
        labId: row.labId,
        bookingTime: [row.startDate, row.endDate],
        remark: row.remark,
      };
      this.editDialogOpen = true;
    },
    /** 提交修改 */
    submitEdit() {
      this.$refs["editForm"].validate((valid) => {
        if (valid) {
          const data = {
            reserveId: this.editForm.reserveId,
            labId: this.editForm.labId,
            startDate: this.editForm.bookingTime[0],
            endDate: this.editForm.bookingTime[1],
            remark: this.editForm.remark,
          };
          updateReservation(data).then((response) => {
            this.$modal.msgSuccess(response.msg || "修改成功");
            this.editDialogOpen = false;
            this.getList();
          });
        }
      });
    },
    /** 撤销预约 */
    handleCancel(row) {
      this.$modal.confirm('是否确认撤销预约编号为"' + row.reserveId + '"的数据项？').then(function () {
        return cancelReservation(row.reserveId);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("撤销成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download("reservation/export", { ...this.queryParams }, `myreservation_${new Date().getTime()}.xlsx`);
    },
  },
};
</script>
