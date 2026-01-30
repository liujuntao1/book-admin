<template>
  <div class="p-4">
    <el-card>
      <el-button type="primary" @click="openDialog()">新增公告</el-button>
    </el-card>

    <el-card class="mt-4">
      <el-table :data="notices" style="width: 100%" stripe>
        <el-table-column prop="title" label="标题" />
        <!-- <el-table-column prop="publisher" label="发布人" /> -->
        <el-table-column prop="publishTime" label="发布时间" />
        <el-table-column label="状态">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'info'">
              {{ scope.row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="250">
          <template #default="scope">
            <el-button size="small" type="info" @click="viewDetail(scope.row)">查看详情</el-button>
            <el-button size="small" @click="openDialog(scope.row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(scope.row.id)"
              >删除</el-button
            >
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        layout="total, prev, pager, next"
        :total="total"
        :page-size="pageSize"
        @current-change="handlePageChange"
        class="mt-4"
      />
    </el-card>

    <!-- 编辑 / 新增 -->
    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑公告' : '新增公告'" width="600px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="标题">
          <el-input v-model="form.title" />
        </el-form-item>
        <!-- <el-form-item label="发布人">
            <el-input v-model="form.publisher" />
          </el-form-item> -->
        <el-form-item label="发布时间">
          <el-date-picker
            v-model="form.publishTime"
            type="datetime"
            value-format="YYYY-MM-DD HH:mm:ss"
          />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status" placeholder="请选择状态">
            <el-option label="启用" value="1" />
            <el-option label="停用" value="0" />
          </el-select>
        </el-form-item>
        <el-form-item label="公告图片">
          <el-upload :show-file-list="false" accept="image/*" :before-upload="handleImageUpload">
            <el-button type="primary">上传图片</el-button>
            <div v-if="form.coverUrl" style="margin-top: 10px">
              <img :src="form.coverUrl" style="width: 100px; height: 140px; object-fit: cover" />
            </div>
          </el-upload>
        </el-form-item>
        <el-form-item label="内容">
          <el-input v-model="form.content" type="textarea" rows="6" placeholder="请输入公告内容" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">保存</el-button>
      </template>
    </el-dialog>

    <!-- 公告详情 -->
    <el-dialog v-model="detailDialogVisible" title="公告详情" width="800px">
      <el-descriptions column="2" border>
        <el-descriptions-item label="标题">{{ selectedNotice.title }}</el-descriptions-item>
        <el-descriptions-item label="发布人">{{ selectedNotice.publisher }}</el-descriptions-item>
        <el-descriptions-item label="发布时间">{{
          selectedNotice.publishTime
        }}</el-descriptions-item>
        <el-descriptions-item label="状态">{{ selectedNotice.status }}</el-descriptions-item>
        <el-descriptions-item label="内容" :span="2">{{
          selectedNotice.content
        }}</el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <el-button @click="detailDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getNoticeList, createNotice, updateNotice, deleteNotice } from '@/api/notice'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()

const notices = ref([])
const total = ref(0)
const pageSize = ref(10)
const currentPage = ref(1)

const dialogVisible = ref(false)
const detailDialogVisible = ref(false)
const selectedNotice = ref({})
const form = ref({
  title: '',
  content: '',
  publisher: '',
  publishTime: '',
  status: '启用',
})

const fetchNotices = async () => {
  const { data } = await getNoticeList({ page: currentPage.value, size: pageSize.value })
  notices.value = data.records
  total.value = data.total
}

const handlePageChange = (page: number) => {
  currentPage.value = page
  fetchNotices()
}

const openDialog = (notice) => {
  if (notice) {
    form.value = { ...notice }
  } else {
    form.value = {
      title: '',
      content: '',
      status: '1',
      publisher: userStore.userInfo?.username || '未知用户', // 自动填入当前用户
      publishTime: new Date().toISOString().slice(0, 10), // 默认填入当前日期
    }
  }
  dialogVisible.value = true
}

const handleSubmit = async () => {
  if (form.value.id) {
    await updateNotice(form.value)
  } else {
    await createNotice(form.value)
  }
  dialogVisible.value = false
  fetchNotices()
}

const handleDelete = async (id: number) => {
  await deleteNotice(id)
  fetchNotices()
}

const viewDetail = (notice) => {
  selectedNotice.value = { ...notice }
  detailDialogVisible.value = true
}

const handleImageUpload = (file) => {
  const reader = new FileReader()
  reader.readAsDataURL(file)
  reader.onload = () => {
    form.value.coverUrl = reader.result // base64 编码
  }
  return false // 阻止自动上传
}

onMounted(() => {
  fetchNotices()
})
</script>

<style scoped>
.mt-4 {
  margin-top: 1rem;
}
</style>
