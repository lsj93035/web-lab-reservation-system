<template>
  <div class="app-container">
    <div style="font-size: 20px; font-weight: bold; color: #303133; margin-bottom: 20px; border-left: 5px solid #67C23A; padding-left: 15px;">
      实验室使用预约申请
    </div>

    <el-card class="box-card" style="margin-bottom: 20px; background-color: #f0f9eb;">
      <el-row :gutter="20">
        <el-col :span="8">
          <div style="color: #606266;"><strong>申请人姓名：</strong>{{ studentInfo.name }}</div>
        </el-col>
        <el-col :span="8">
          <div style="color: #606266;"><strong>学号：</strong>{{ studentInfo.id }}</div>
        </el-col>
        <el-col :span="8">
          <div style="color: #606266;"><strong>所属学院：</strong>信息工程学院</div>
        </el-col>
      </el-row>
    </el-card>

    <el-card shadow="never">
      <el-form ref="form" :model="form" :rules="rules" label-width="120px" style="max-width: 600px; margin-top: 20px;">
        <el-form-item label="目标实验室" prop="labName">
          <el-select v-model="form.labName" placeholder="请选择要预约的实验室" style="width: 100%">
            <el-option label="人工智能实验室 (302)" value="人工智能实验室 (302)" />
            <el-option label="物联网实训室 (405)" value="物联网实训室 (405)" />
            <el-option label="大数据中心 (501)" value="大数据中心 (501)" />
          </el-select>
        </el-form-item>

        <el-form-item label="使用日期" prop="reserveDate">
          <el-date-picker
            v-model="form.reserveDate"
            type="date"
            placeholder="选择预约日期"
            style="width: 100%"
            value-format="yyyy-MM-dd"
          />
        </el-form-item>

        <el-form-item label="使用时段" prop="timeSlot">
          <el-radio-group v-model="form.timeSlot">
            <el-radio label="上午 (08:00-12:00)">上午</el-radio>
            <el-radio label="下午 (14:00-18:00)">下午</el-radio>
            <el-radio label="晚上 (19:00-22:00)">晚上</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="申请用途" prop="purpose">
          <el-input
            v-model="form.purpose"
            type="textarea"
            :rows="4"
            placeholder="请详细描述实验内容或科研项目名称"
          />
        </el-form-item>

        <el-form-item>
          <el-button type="success" icon="el-icon-s-promotion" @click="submitForm">提交预约申请</el-button>
          <el-button @click="resetForm">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <div style="margin-top: 30px;">
      <div style="font-weight: bold; margin-bottom: 10px; color: #909399;">我的预约历史</div>
      <el-table :data="myHistory" border size="mini" style="width: 100%">
        <el-table-column prop="date" label="申请日期" width="120" />
        <el-table-column prop="lab" label="实验室" />
        <el-table-column prop="status" label="审批状态" width="100">
          <template slot-scope="scope">
            <el-tag size="mini" :type="scope.row.type">{{ scope.row.status }}</el-tag>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
export default {
  name: "StudentReserve",
  data() {
    return {
      studentInfo: {
        name: "刘雷哲",
        id: "2024090123"
      },
      form: {
        labName: '',
        reserveDate: '',
        timeSlot: '',
        purpose: ''
      },
      rules: {
        labName: [{ required: true, message: '请选择实验室', trigger: 'change' }],
        reserveDate: [{ required: true, message: '请选择日期', trigger: 'change' }],
        timeSlot: [{ required: true, message: '请选择时段', trigger: 'change' }],
        purpose: [{ required: true, message: '请填写用途', trigger: 'blur' }]
      },
      myHistory: [
        { date: '2026-04-20', lab: '人工智能实验室', status: '已通过', type: 'success' },
        { date: '2026-04-24', lab: '大数据中心', status: '待审核', type: 'info' }
      ]
    };
  },
  methods: {
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.$modal.msgSuccess("申请提交成功！请等待刘雷哲同学的指导老师审核");
          // 模拟提交后清空表单
          this.resetForm();
        }
      });
    },
    resetForm() {
      this.$refs.form.resetFields();
    }
  }
};
</script>

<style scoped>
.box-card {
  border-radius: 10px;
}
</style>