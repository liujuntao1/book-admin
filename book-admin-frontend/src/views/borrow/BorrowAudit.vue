<template>
    <div class="p-4">
      <el-card>
    <!-- Tab页签 -->
    <el-tabs v-model="activeTab" @tab-change="fetchData">
      <el-tab-pane label="待审核" name="pending" />
      <el-tab-pane label="已通过" name="approved" />
      <el-tab-pane label="已拒绝" name="rejected" />
    </el-tabs>
  </el-card>
  
  <el-card class="mt-4">
    <!-- 借阅记录表格 -->
    <el-table :data="borrowList" stripe style="width: 100%" v-loading="loading">
      <el-table-column prop="id" label="编号" width="80" />
      <el-table-column prop="bookTitle" label="图书名称" />
      <el-table-column prop="userName" label="申请人" />
      <el-table-column prop="borrowDate" label="借阅日期" />
      <el-table-column prop="returnDate" label="归还时间" />
      <el-table-column prop="status" label="状态">
        <template #default="{ row }">
          <el-tag v-if="row.status === 0" type="warning">待审核</el-tag>
          <el-tag v-else-if="row.status === 1" type="success">已通过</el-tag>
          <el-tag v-else-if="row.status === 2" type="danger">已拒绝</el-tag>
          <el-tag v-else type="primary">已归还</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="160">
        <template #default="{ row }">
          <el-button
            v-if="row.status === 0"
            size="small"
            type="primary"
            @click="openAuditDialog(row)"
            >审核</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
      v-model:current-page="page"
      :page-size="size"
      :total="total"
      layout="prev, pager, next"
      @current-change="fetchData"
      class="mt-4"
    />
  </el-card>
    <!-- 审核弹窗 -->
    <el-dialog v-model="auditDialogVisible" title="审核申请" width="500px">
      <el-form :model="auditForm" label-width="80px">
        <el-form-item label="审核结果">
          <el-radio-group v-model="auditForm.status">
            <el-radio :label="1">通过</el-radio>
            <el-radio :label="2">拒绝</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item v-if="auditForm.status === 2" label="拒绝原因">
          <el-input v-model="auditForm.rejectionReason" type="textarea" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="auditDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitAudit">提交</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { auditList, approve, reject } from '@/api/bookBorrow'

const activeTab = ref('pending')
const borrowList = ref([])
const loading = ref(false)
const page = ref(1)
const size = ref(10)
const total = ref(0)

const auditDialogVisible = ref(false)
const auditForm = ref({
  id: null,
  status: 1,
  rejectionReason: '',
})

const fetchData = async () => {
  loading.value = true
  // 根据 activeTab 直接确定 status
  const statusMap = {
    pending: 0,
    approved: 1,
    rejected: 2,
  }
  const status = statusMap[activeTab.value] ?? 1 // 默认 1（已审核）
  // 请求借阅审核列表（一次性取完）
  const { data } = await auditList({ page: page.value, size: size.value, status })
  borrowList.value = data.records
  total.value = data.total // 总条数，用于分页显示
  loading.value = false
}

const openAuditDialog = (record: BorrowRecord) => {
  auditForm.value.id = record.id
  auditForm.value.status = 1
  auditForm.value.rejectionReason = ''
  auditDialogVisible.value = true
}

const submitAudit = async () => {
  // 模拟提交逻辑
  console.log('提交审核结果：', auditForm.value)
  if (auditForm.value.status === 1) {
    await approve(auditForm.value.id) // 一次取完
  } else {
    await reject(auditForm.value.id, auditForm.value.rejectionReason) // 一次取完
  }
  ElMessage.success('审核成功')
  auditDialogVisible.value = false
  fetchData()
}

onMounted(() => {
  fetchData()
})
</script>
<style scoped>
.mt-4 {
  margin-top: 1rem;
}</style>
