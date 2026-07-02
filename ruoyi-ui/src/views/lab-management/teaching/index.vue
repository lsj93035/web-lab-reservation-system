<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="实验室" prop="labId">
        <el-select v-model="queryParams.labId" placeholder="请选择实验室" clearable filterable>
          <el-option v-for="item in labList" :key="item.labId" :label="item.labName" :value="item.labId" />
        </el-select>
      </el-form-item>
      <el-form-item label="课程名称" prop="courseName">
        <el-input v-model="queryParams.courseName" placeholder="请输入课程名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="占用日期" prop="occupyDate">
        <el-date-picker v-model="queryParams.occupyDate" type="date" placeholder="选择日期" value-format="yyyy-MM-dd" clearable />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['lab:teaching:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :multiple="true" @click="handleDelete" v-hasPermi="['lab:teaching:remove']">删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="occupancyList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="占用编号" align="center" prop="occupyId" />
      <el-table-column label="实验室名称" align="center" prop="labName" />
      <el-table-column label="所在楼宇" align="center" prop="building" />
      <el-table-column label="课程名称" align="center" prop="courseName" />
      <el-table-column label="教师姓名" align="center" prop="teacherName" />
      <el-table-column label="占用日期" align="center" prop="occupyDate" width="120">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.occupyDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="使用时段" align="center" prop="timeSlot" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['lab:teaching:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 新增教学占用对话框 -->
    <el-dialog title="新增教学占用" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="实验室" prop="labId">
          <el-select v-model="form.labId" placeholder="请选择实验室" filterable>
            <el-option v-for="item in labList" :key="item.labId" :label="item.labName" :value="item.labId" />
          </el-select>
        </el-form-item>
        <el-form-item label="课程名称" prop="courseName">
          <el-input v-model="form.courseName" placeholder="请输入课程名称" />
        </el-form-item>
        <el-form-item label="占用日期" prop="occupyDate">
          <el-date-picker v-model="form.occupyDate" type="date" placeholder="选择日期" value-format="yyyy-MM-dd" />
        </el-form-item>
        <el-form-item label="使用时段" prop="timeSlot">
          <el-input v-model="form.timeSlot" placeholder="如 08:00-10:00" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listTeaching, addTeaching, delTeaching } from "@/api/lab-management/teaching";
import { listLab } from "@/api/lab-management/lab";

export default {
  name: "Teaching",
  data() {
    return {
      loading: true,
      showSearch: true,
      total: 0,
      occupancyList: [],
      labList: [],
      ids: [],
      single: true,
      multiple: true,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        labId: null,
        courseName: null,
        occupyDate: null,
      },
      open: false,
      form: {
        labId: null,
        courseName: null,
        occupyDate: null,
        timeSlot: null,
      },
      rules: {
        labId: [{ required: true, message: "请选择实验室", trigger: "change" }],
        courseName: [{ required: true, message: "请输入课程名称", trigger: "blur" }],
        occupyDate: [{ required: true, message: "请选择占用日期", trigger: "change" }],
        timeSlot: [{ required: true, message: "请输入使用时段", trigger: "blur" }],
      },
    };
  },
  created() {
    this.getList();
    this.getLabList();
  },
  methods: {
    /** 查询列表 */
    getList() {
      this.loading = true;
      listTeaching(this.queryParams).then((response) => {
        this.occupancyList = response.rows;
        this.total = response.total;
      }).catch(() => {
        this.occupancyList = [];
        this.total = 0;
      }).finally(() => {
        this.loading = false;
      });
    },
    /** 获取实验室列表 */
    getLabList() {
      listLab({ pageSize: 100 }).then((response) => {
        this.labList = response.rows;
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
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.occupyId);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          addTeaching(this.form).then((response) => {
            this.$modal.msgSuccess(response.msg || "新增成功");
            this.open = false;
            this.getList();
          });
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const occupyIds = row.occupyId || this.ids;
      this.$modal.confirm('是否确认删除教学占用编号为"' + occupyIds + '"的数据项？').then(function () {
        return delTeaching(occupyIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    // 表单重置
    reset() {
      this.form = {
        labId: null,
        courseName: null,
        occupyDate: null,
        timeSlot: null,
      };
      this.resetForm("form");
    },
    cancel() {
      this.open = false;
      this.reset();
    },
  },
};
</script>
