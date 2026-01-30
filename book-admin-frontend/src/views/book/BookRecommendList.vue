<template>
  <div class="p-4">
    <el-card>
      <el-button type="primary" @click="openDialog()">新增推荐</el-button>
    </el-card>

    <el-card class="mt-4">
      <el-table :data="recommendations" style="width: 100%" stripe>
        <!-- 新增：图书封面 -->
        <el-table-column label="封面" width="100">
          <template #default="scope">
            <el-image
              style="width: 60px; height: 80px"
              :src="scope.row.coverUrl"
              :preview-src-list="[scope.row.coverUrl]"
              fit="cover"
            />
          </template>
        </el-table-column>
        <el-table-column prop="bookTitle" label="图书名称" />
        <el-table-column prop="type" label="推荐类型">
          <template #default="scope">
            <el-tag
              :type="scope.row.type === 1 ? 'success' : scope.row.type === 2 ? 'info' : 'warning'"
            >
              {{ scope.row.type === 1 ? '热门' : scope.row.type === 2 ? '新书' : '分类' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="reason" label="推荐理由" />
        <el-table-column prop="sort" label="排序" />
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
    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑推荐' : '新增推荐'" width="600px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="图书名称">
          <!-- 下拉框，选择图书 -->
          <el-select v-model="form.bookId" placeholder="请选择图书" filterable>
            <el-option v-for="book in books" :key="book.id" :label="book.title" :value="book.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="推荐类型">
          <el-select v-model="form.type" placeholder="请选择推荐类型">
            <el-option label="热门" value="1" />
            <el-option label="新书" value="2" />
            <el-option label="分类" value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="推荐理由">
          <el-input v-model="form.reason" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="form.sort" :min="0" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">保存</el-button>
      </template>
    </el-dialog>

    <!-- 推荐图书详情 -->
    <el-dialog v-model="detailDialogVisible" title="推荐图书详情" width="800px">
      <el-descriptions column="2" border>
        <el-descriptions-item label="图书名称">{{
          selectedRecommendation.bookTitle
        }}</el-descriptions-item>
        <el-descriptions-item label="推荐类型">{{
          selectedRecommendation.type === 1
            ? '热门'
            : selectedRecommendation.type === 2
              ? '新书'
              : '分类'
        }}</el-descriptions-item>
        <el-descriptions-item label="推荐理由">{{
          selectedRecommendation.reason
        }}</el-descriptions-item>
        <el-descriptions-item label="排序">{{ selectedRecommendation.sort }}</el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <el-button @click="detailDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>
<script setup lang="ts">
import { ref, onMounted } from 'vue'
import {
  getBookRecommendationList,
  createRecommendation,
  updateRecommendation,
  deleteRecommendation,
} from '@/api/recommendation'
import { getBookList } from '@/api/book' // 导入图书列表接口
import { ElMessageBox, ElMessage } from 'element-plus'

const books = ref([]) // 用于存储图书列表
const recommendations = ref([])
const total = ref(0)
const pageSize = ref(10)
const currentPage = ref(1)

const dialogVisible = ref(false)
const detailDialogVisible = ref(false)
const selectedRecommendation = ref({})
const form = ref({
  bookTitle: '',
  type: '1',
  reason: '',
  sort: 0,
})

const fetchRecommendations = async () => {
  const { data } = await getBookRecommendationList({
    page: currentPage.value,
    size: pageSize.value,
  })
  recommendations.value = data.records
  total.value = data.total
}

const handlePageChange = (page: number) => {
  currentPage.value = page
  fetchRecommendations()
}

const openDialog = (recommendation = null) => {
  if (recommendation) {
    form.value = { ...recommendation }
  } else {
    form.value = {
      bookTitle: '', // 这个字段会通过接口自动填充
      type: '1', // 默认热门
      reason: '',
      sort: 0,
    }
  }
  dialogVisible.value = true
}

const handleSubmit = async () => {
  if (form.value.id) {
    const res = await updateRecommendation(form.value)
    if (res.code == 200) {
      ElMessage.success('修改成功')
    } else {
      ElMessage.error('修改失败')
    }
  } else {
    const res = await createRecommendation(form.value)
    if (res.code == 200) {
      ElMessage.success('修改成功')
    } else {
      ElMessage.error('修改失败')
    }
  }
  dialogVisible.value = false
  fetchRecommendations()
}

const handleDelete = async (id: number) => {
  // 弹出确认框
  ElMessageBox.confirm('您确定要删除吗?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  })
    .then(async () => {
      // 用户点击确定后执行删除
      const res = await deleteRecommendation(id)
      if (res.code == 200) {
        ElMessage.success('删除成功')
      } else {
        ElMessage.error('删除失败')
      }
      // 删除成功后刷新列表
      fetchRecommendations()
    })
    .catch(() => {
      // 用户点击取消
      ElMessage.info('删除已取消')
    })
}

const viewDetail = (recommendation) => {
  selectedRecommendation.value = { ...recommendation }
  detailDialogVisible.value = true
}
// 获取图书列表
const fetchBooks = async () => {
  try {
    const { data } = await getBookList({ page: 1, size: 100 }) // 获取图书列表
    books.value = data.records // 假设返回的图书列表是 data.records
  } catch (error) {
    console.error('获取图书列表失败', error)
  }
}

// 组件挂载后调用接口获取图书列表
onMounted(() => {
  fetchRecommendations()
  fetchBooks()
})
</script>
<style scoped>
.mt-4 {
  margin-top: 1rem;
}
</style>
