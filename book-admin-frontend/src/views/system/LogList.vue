<template>
  <div class="p-4">
    <el-card>
      <el-form :model="queryParams" inline @submit.prevent>
        <el-form-item label="用户名">
          <el-input v-model="queryParams.username" placeholder="请输入用户名" clearable />
        </el-form-item>
        <el-form-item label="请求方式" style="width: 300px">
          <el-select v-model="queryParams.method" placeholder="请选择" clearable>
            <el-option label="GET" value="GET" />
            <el-option label="POST" value="POST" />
            <el-option label="PUT" value="PUT" />
            <el-option label="DELETE" value="DELETE" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" style="width: 300px">
          <el-select v-model="queryParams.status" placeholder="请选择" clearable>
            <el-option label="成功" value="1" />
            <el-option label="失败" value="0" />
          </el-select>
        </el-form-item>
        <el-form-item label="操作时间">
          <el-date-picker
            v-model="queryParams.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="YYYY-MM-DD"
            unlink-panels
            clearable
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="getList">查询</el-button>
          <el-button @click="resetForm">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="mt-4">
      <el-table :data="tableData.records" stripe>
        <el-table-column prop="username" label="用户名" width="120" />
        <el-table-column prop="ip" label="IP地址" width="140" />
        <el-table-column prop="uri" label="请求URI" width="300" />
        <el-table-column prop="method" label="请求方式" width="90" />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '成功' : '失败' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="操作时间" width="400" />
        <el-table-column prop="errorMsg" label="异常信息" />
        <el-table-column label="操作" width="100">
          <template #default="{ row }">
            <el-button type="primary" link @click="openDetail(row)">查看</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="mt-4 text-right">
        <el-pagination
          v-model:current-page="queryParams.page"
          v-model:page-size="queryParams.size"
          :total="tableData.total"
          @current-change="getList"
          @size-change="getList"
          layout="total, sizes, prev, pager, next, jumper"
        />
      </div>
    </el-card>
    <!-- 弹窗：日志详情 -->
    <el-dialog v-model="dialogVisible" title="日志详情" width="800px">
      <el-descriptions :column="1" size="default" border label-width="120px">
        <el-descriptions-item label="用户名">{{ detailData.username }}</el-descriptions-item>
        <el-descriptions-item label="IP地址">{{ detailData.ip }}</el-descriptions-item>
        <el-descriptions-item label="请求URI">{{ detailData.uri }}</el-descriptions-item>
        <el-descriptions-item label="请求方式">{{ detailData.method }}</el-descriptions-item>
        <el-descriptions-item label="请求参数">
          <pre style="white-space: pre-wrap; word-break: break-word">{{ detailData.params }}</pre>
        </el-descriptions-item>
        <el-descriptions-item label="响应结果">
          <pre style="white-space: pre-wrap; word-break: break-word">{{ detailData.result }}</pre>
        </el-descriptions-item>
        <el-descriptions-item label="异常信息">{{
          detailData.errorMsg || '无'
        }}</el-descriptions-item>
        <el-descriptions-item label="操作时间">{{ detailData.createTime }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { fetchLogList } from '@/api/log'

const queryParams = ref({
  username: '',
  method: '',
  status: '',
  dateRange: [],
  page: 1,
  size: 10,
})

const tableData = ref({
  records: [],
  total: 0,
})
const getList = async () => {
  try {
    const { username, method, status, dateRange, page, size } = queryParams.value
    const [startTime, endTime] = dateRange || []
    const res = await fetchLogList({
      username,
      method,
      status: status ? Number(status) : undefined,
      startTime,
      endTime,
      page,
      size,
    })
    tableData.value = res.data
  } catch (e) {
    ElMessage.error('日志加载失败')
  }
}

const resetForm = () => {
  queryParams.value = {
    username: '',
    method: '',
    status: '',
    dateRange: [],
    page: 1,
    size: 10,
  }
  getList()
}

getList()

const dialogVisible = ref(false)
const detailData = ref({
  id: 0,
  username: '',
  ip: '',
  uri: '',
  method: '',
  params: '',
  result: '',
  status: 1,
  errorMsg: '',
  createTime: '',
})

const openDetail = (row) => {
  detailData.value = { ...row }
  dialogVisible.value = true
}
</script>

<style scoped>
.mt-4 {
  margin-top: 1rem;
}
</style>
