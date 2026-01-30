<template>
  <div class="book-detail">
    <!-- 返回按钮 -->
    <div class="back-button">
      <el-button type="text" @click="goBack">
        <el-icon><ArrowLeft /></el-icon>
        返回
      </el-button>
    </div>

    <!-- 图书卡片内容 -->
    <el-card>
      <div class="book-cover">
        <el-image :src="book.coverUrl" style="width: 200px; height: 300px; object-fit: cover" />
      </div>
      <div class="book-info">
        <el-descriptions title="图书详情" column="2" label-width="120px" border>
          <el-descriptions-item label="书名">{{ book.title }}</el-descriptions-item>
          <el-descriptions-item label="作者">{{ book.author }}</el-descriptions-item>
          <el-descriptions-item label="出版社">{{ book.publisher }}</el-descriptions-item>
          <el-descriptions-item label="库存数量">{{ book.stock }}</el-descriptions-item>
          <el-descriptions-item label="出版日期">{{ book.publishTime }}</el-descriptions-item>
          <el-descriptions-item label="ISBN">{{ book.isbn }}</el-descriptions-item>
          <el-descriptions-item label="描述">{{ book.description }}</el-descriptions-item>
        </el-descriptions>
        <div class="book-actions">
          <el-button v-if="!isBorrowed" type="primary" @click="applyForBorrowing">
            申请借阅
          </el-button>
          <el-button v-else type="danger" disabled> 已借阅 </el-button>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router' // 导入 vue-router
import { getBookById } from '@/api/book' // 假设你有一个 API 接口获取图书详情
import { apply, isBeingBorrowed } from '@/api/bookBorrow'
import { ElMessage } from 'element-plus'
import { ArrowLeft } from '@element-plus/icons-vue'

const isBorrowed = ref(false)
const route = useRoute()
const router = useRouter() // 使用 vue-router 的 useRouter
const book = ref({})
const form = ref({
  bookId: '',
  userId: '',
  username: '',
})

// 获取图书详情数据
const fetchBookDetail = async () => {
  const { data } = await getBookById(route.params.id)
  book.value = data
  form.value.bookId = data.id // ✅ 在这里设置 bookId
  await getButtonStatus() // ✅ 获取按钮状态
}

const applyForBorrowing = async () => {
  // 在此可以调用借阅申请的接口
  const res = await apply(form.value)
  if (res.code === 200) {
    ElMessage.success('申请成功，等待审批')
  } else {
    ElMessage.error(res.msg || '提交失败')
  }
  getButtonStatus()
}

const goBack = () => {
  const from = route.query.from
  if (from) {
    router.push(from)
  } else {
    router.push('/borrow/readerBookList') // 默认回退路径
  }
}

const getButtonStatus = async () => {
  const res = await isBeingBorrowed(form.value)
  isBorrowed.value = res.data
}

onMounted(() => {
  const user = localStorage.getItem('userInfo')
  if (user) {
    form.value.username = JSON.parse(user).username
    form.value.userId = JSON.parse(user).id
  }
  fetchBookDetail() // ✅ 此时再获取图书详情
})
</script>

<style scoped>
.book-detail {
  padding: 20px;
}

.book-cover {
  text-align: center;
  margin-bottom: 20px;
}

.book-info h2 {
  font-size: 24px;
  font-weight: bold;
}

.book-actions {
  margin-top: 20px;
}

.back-button {
  margin-bottom: 10px;
}
</style>
