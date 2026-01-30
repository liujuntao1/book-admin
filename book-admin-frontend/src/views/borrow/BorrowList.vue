<template>
    <div class="p-4">
      <el-card>
    <template #header>
      <div class="flex justify-between items-center">
        <span>借阅记录</span>
      </div>
    </template>
  </el-card>
  
  <el-card class="mt-4">
    <el-table :data="tableData" stripe style="width: 100%">
      <el-table-column prop="bookTitle" label="图书名称" />
      <el-table-column prop="userName" label="借阅人" />
      <el-table-column prop="borrowDate" label="借阅日期" />
      <el-table-column prop="returnDate" label="归还日期" />
      <el-table-column prop="status" label="状态">
        <template #default="{ row }">
          <el-tag :type="statusTypeMap[row.status]">
            {{ statusMap[row.status] }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="rejectionReason" label="拒绝原因" />
      <el-table-column label="操作" width="160">
        <template #default="{ row }">
          <el-button
            v-if="row.status === 1"
            size="small"
            type="primary"
            @click="handleReturn(row.id)"
            >还书</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      class="mt-4"
      v-model:current-page="page"
      v-model:page-size="pageSize"
      :total="total"
      layout="prev, pager, next, jumper"
      @current-change="loadData"
    />
  </el-card>
</div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { borrowList, returnBook } from '@/api/bookBorrow'
import { ElMessage, ElMessageBox } from 'element-plus'
const tableData = ref([])
const page = ref(1)
const pageSize = ref(10)
const total = ref(0)

const statusMap = {
  0: '申请中',
  1: '已借出',
  2: '已拒绝',
  3: '已归还',
}

const statusTypeMap = {
  0: 'info',
  1: 'success',
  2: 'danger',
  3: 'info',
}

const loadData = async () => {
  // 请求借阅审核列表（一次性取完）
  const { data } = await borrowList({ page: page.value, size: pageSize.value })
  tableData.value = data.records
  total.value = data.total
}
const handleReturn = (borrowId: number) => {
  ElMessageBox.confirm('确认归还这本书吗？', '提示', {
    confirmButtonText: '确认',
    cancelButtonText: '取消',
    type: 'warning',
  })
    .then(async () => {
      await returnBook(borrowId)
      ElMessage.success('归还成功')
      loadData() // 刷新列表
    })
    .catch(() => {
      // 用户取消
    })
}

onMounted(loadData)
</script>
<style scoped>
.mt-4 {
  margin-top: 1rem;
}</style>
