<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="实验室名称" prop="labName">
        <el-input v-model="queryParams.labName" placeholder="请输入实验室名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="所在楼宇" prop="building">
        <el-select v-model="queryParams.building" placeholder="请选择楼宇" clearable>
          <el-option v-for="item in buildingList" :key="item" :label="item" :value="item" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option v-for="dict in dict.type.lab_status" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="设备" prop="equipment">
        <el-input v-model="queryParams.equipment" placeholder="请输入设备关键字" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['lab:lab:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :multiple="true" @click="handleDelete" v-hasPermi="['lab:lab:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['lab:lab:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="labList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
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
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['lab:lab:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['lab:lab:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 添加或修改实验室对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="实验室名称" prop="labName">
          <el-input v-model="form.labName" placeholder="请输入实验室名称" />
        </el-form-item>
        <el-form-item label="所在楼宇" prop="building">
          <el-input v-model="form.building" placeholder="请输入所在楼宇" />
        </el-form-item>
        <el-form-item label="容纳人数" prop="capacity">
          <el-input-number v-model="form.capacity" :min="1" :max="9999" placeholder="请输入容纳人数" />
        </el-form-item>
        <el-form-item label="设备清单" prop="equipment">
          <el-input v-model="form.equipment" type="textarea" placeholder="请输入设备清单" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio v-for="dict in dict.type.lab_status" :key="dict.value" :label="parseInt(dict.value)">{{ dict.label }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" />
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
import { listLab, getLab, addLab, updateLab, delLab, changeLabStatus, getBuildingList } from "@/api/lab-management/lab";

export default {
  name: "Lab",
  dicts: ['lab_status'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 实验室表格数据
      labList: [],
      // 楼宇列表
      buildingList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        labName: null,
        building: null,
        status: null,
        equipment: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        labName: [{ required: true, message: "实验室名称不能为空", trigger: "blur" }],
        building: [{ required: true, message: "所在楼宇不能为空", trigger: "blur" }],
        capacity: [{ required: true, message: "容纳人数不能为空", trigger: "blur" }],
      },
    };
  },
  created() {
    this.getList();
    this.getBuildingList();
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
    /** 获取楼宇列表 */
    getBuildingList() {
      getBuildingList().then((response) => {
        this.buildingList = response.data;
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
      this.ids = selection.map((item) => item.labId);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加实验室";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const labId = row.labId || this.ids;
      getLab(labId).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改实验室";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.labId != null) {
            updateLab(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addLab(this.form).then((response) => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const labIds = row.labId || this.ids;
      this.$modal.confirm('是否确认删除实验室编号为"' + labIds + '"的数据项？').then(function () {
        return delLab(labIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download("lab/export", { ...this.queryParams }, `lab_${new Date().getTime()}.xlsx`);
    },
    // 表单重置
    reset() {
      this.form = {
        labId: null,
        labName: null,
        building: null,
        capacity: null,
        status: 0,
        equipment: null,
        remark: null,
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
