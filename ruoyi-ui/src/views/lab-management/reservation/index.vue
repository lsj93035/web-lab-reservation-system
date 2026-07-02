<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="实验室名称" prop="labName">
        <el-input v-model="queryParams.labName" placeholder="请输入实验室名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="所在楼宇" prop="building">
        <el-input v-model="queryParams.building" placeholder="请输入楼宇" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="设备" prop="equipment">
        <el-input v-model="queryParams.equipment" placeholder="请输入设备关键字" clearable @keyup.enter.native="handleQuery" />
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
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="200">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleViewTime(scope.row)">查看预约状态</el-button>
          <el-button size="mini" type="text" icon="el-icon-plus" @click="handleBooking(scope.row)" v-hasPermi="['lab:reservation:add']" :disabled="scope.row.status != 0">进行预约</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 查看预约状态对话框 -->
    <el-dialog title="查看预约状态" :visible.sync="timeDialogOpen" width="600px" append-to-body>
      <el-form label-width="100px">
        <el-form-item label="实验室名称">
          <el-input v-model="currentLab.labName" disabled />
        </el-form-item>
        <el-form-item label="所在楼宇">
          <el-input v-model="currentLab.building" disabled />
        </el-form-item>
        <el-form-item label="选择日期">
          <el-date-picker v-model="queryDate" type="date" placeholder="选择日期" value-format="yyyy-MM-dd" @change="handleDateChange" />
        </el-form-item>
      </el-form>
      <el-divider content-position="center">已预约时间段</el-divider>
      <div v-if="timeList.length > 0">
        <el-tag v-for="(item, index) in timeList" :key="index" type="danger" style="margin: 5px;">
          {{ formatTime(item.startDate) }} - {{ formatTime(item.endDate) }}
        </el-tag>
      </div>
      <div v-else style="text-align: center; color: #999;">暂无预约记录</div>
    </el-dialog>

    <!-- 预约对话框 -->
    <el-dialog title="预约实验室" :visible.sync="bookingDialogOpen" width="600px" append-to-body>
      <el-form ref="bookingForm" :model="bookingForm" :rules="bookingRules" label-width="100px">
        <el-form-item label="实验室名称">
          <el-input v-model="currentLab.labName" disabled />
        </el-form-item>
        <el-form-item label="所在楼宇">
          <el-input v-model="currentLab.building" disabled />
        </el-form-item>
        <el-form-item label="预约时间" prop="bookingTime">
          <el-date-picker v-model="bookingForm.bookingTime" type="datetimerange" range-separator="至" start-placeholder="开始时间" end-placeholder="结束时间" value-format="yyyy-MM-dd HH:mm:ss" :picker-options="datePickerOptions" />
        </el-form-item>
        <el-form-item label="实验备注" prop="remark">
          <el-input v-model="bookingForm.remark" type="textarea" placeholder="请输入实验备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitBooking">确 定</el-button>
        <el-button @click="bookingDialogOpen = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listLab } from "@/api/lab-management/lab";
import { timeQueryByDate, addReservation } from "@/api/lab-management/reservation";

export default {
  name: "Reservation",
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
        equipment: null,
      },
      // 查看预约状态
      timeDialogOpen: false,
      currentLab: {},
      queryDate: null,
      timeList: [],
      // 预约表单
      bookingDialogOpen: false,
      bookingForm: {
        labId: null,
        bookingTime: null,
        remark: null,
      },
      bookingRules: {
        bookingTime: [{ required: true, message: "请选择预约时间", trigger: "blur" }],
      },
      datePickerOptions: {
        disabledDate(time) {
          return time.getTime() < Date.now() - 8.64e7;
        },
      },
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询可用实验室列表 */
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
    /** 查看预约状态 */
    handleViewTime(row) {
      this.currentLab = row;
      this.queryDate = null;
      this.timeList = [];
      this.timeDialogOpen = true;
    },
    /** 日期变化 */
    handleDateChange(date) {
      if (date && this.currentLab.labId) {
        timeQueryByDate(this.currentLab.labId, date).then((response) => {
          this.timeList = response.data || [];
        });
      }
    },
    /** 进行预约 */
    handleBooking(row) {
      this.currentLab = row;
      this.bookingForm = {
        labId: row.labId,
        bookingTime: null,
        remark: null,
      };
      this.bookingDialogOpen = true;
    },
    /** 提交预约 */
    submitBooking() {
      this.$refs["bookingForm"].validate((valid) => {
        if (valid) {
          const data = {
            labId: this.bookingForm.labId,
            startDate: this.bookingForm.bookingTime[0],
            endDate: this.bookingForm.bookingTime[1],
            remark: this.bookingForm.remark,
          };
          addReservation(data).then((response) => {
            this.$modal.msgSuccess(response.msg || "预约提交成功，等待审核");
            this.bookingDialogOpen = false;
            this.getList();
          });
        }
      });
    },
    /** 格式化时间 */
    formatTime(time) {
      if (!time) return '';
      const date = new Date(time);
      return date.toLocaleString();
    },
  },
};
</script>
