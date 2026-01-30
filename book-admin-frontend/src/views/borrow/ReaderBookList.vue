<template>
  <div class="reader-book-list">
    <!-- 搜索栏 -->
    <el-form :model="queryParams" inline>
      <el-form-item label="书名">
        <el-input v-model="queryParams.title" placeholder="请输入书名" clearable />
      </el-form-item>
      <el-form-item label="作者">
        <el-input v-model="queryParams.author" placeholder="请输入作者" clearable />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="fetchBooks">搜索</el-button>
      </el-form-item>
    </el-form>

    <!-- 图书卡片列表 -->
    <el-row :gutter="20" style="margin-top: 20px">
      <!-- 每行 6 列 -->
      <el-col v-for="book in books" :key="book.id" :xs="8" :sm="8" :md="4" :lg="3">
        <el-card shadow="hover" class="book-card" @click="goToBookDetail(book.id)">
          <div class="card-wrapper">
            <!-- 图书封面图 -->
            <el-image
              :src="book.coverUrl"
              class="book-cover"
              fit="cover"
              :preview-src-list="[book.coverUrl]"
            />
            <div class="book-info">
              <div class="book-title">{{ book.title }}</div>
              <div class="book-author">作者：{{ book.author }}</div>
            </div>
            <div class="book-actions">
              <el-button
                size="small"
                :type="book.isFavorite ? 'warning' : 'default'"
                @click.stop="toggleFavorite(book)"
                icon="Star"
                class="favorite-button"
              >
                {{ book.isFavorite ? '已收藏' : '收藏' }}
              </el-button>
              <el-button
                size="small"
                type="primary"
                @click.stop="goToReviewPage(book.id)"
                icon="ChatDotRound"
                class="review-button"
              >
                评价
              </el-button>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <!-- 分页 -->
    <div style="margin-top: 20px; text-align: right">
      <el-pagination
        v-model:current-page="pageParams.page"
        v-model:page-size="pageParams.pageSize"
        :total="total"
        layout="prev, pager, next"
        @current-change="fetchBooks"
      />
    </div>

    <!-- 弹出评价对话框 -->
    <el-dialog v-model="reviewDialogVisible" title="图书评价" width="500px">
      <el-form :model="reviewForm" label-width="60px">
        <el-form-item label="评分">
          <el-rate v-model="reviewForm.rating" />
        </el-form-item>
        <el-form-item label="内容">
          <el-input
            type="textarea"
            v-model="reviewForm.comment"
            placeholder="请输入评价内容"
            rows="4"
            maxlength="300"
            show-word-limit
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="reviewDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitReview">提交</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script  setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getBookList, addFavorites, removeFavorites } from '@/api/book'
import { useRouter } from 'vue-router' // 导入 vue-router
import { useUserStore } from '@/stores/user'
import { useNavigation } from '@/utils/navigation'

const queryParams = reactive({
  title: '',
  author: '',
  querySource: 1,
})

const pageParams = reactive({
  page: 1,
  size: 16,
})

const books = ref([])
const total = ref(0)

const userStore = useUserStore()
const user = userStore.userInfo || {}

// 获取图书数据
const fetchBooks = async () => {
  const { data } = await getBookList({
    ...queryParams,
    ...pageParams,
  })
  books.value = data.records
  total.value = data.total
}

// 收藏 / 取消收藏
const toggleFavorite = async (book) => {
  //   const res = await toggleFavoriteApi(book.id)
  if (book.isFavorite) {
    const data = await removeFavorites({ userId: user.id, bookId: book.id })
    if (data.code == 200) {
      ElMessage.success('取消收藏')
    } else {
      ElMessage.error('取消收藏失败')
    }
  } else {
    const data = await addFavorites({ userId: user.id, bookId: book.id })
    debugger
    if (data.code == 200) {
      ElMessage.success('已收藏')
    } else {
      ElMessage.error('收藏失败')
    }
  }
  fetchBooks()
}
// 跳转到图书详情页面
const router = useRouter()

// 跳转到图书详情页面
const { pushBookDetail } = useNavigation()
const goToBookDetail = (bookId) => {
  pushBookDetail(bookId)
}

onMounted(fetchBooks)

const reviewDialogVisible = ref(false)
const reviewForm = reactive({
  bookId: null,
  rating: 0,
  comment: '',
})

const openReviewDialog = (book) => {
  reviewForm.bookId = book.id
  reviewForm.rating = 0
  reviewForm.comment = ''
  reviewDialogVisible.value = true
}

const submitReview = async () => {
  if (!reviewForm.rating || !reviewForm.comment.trim()) {
    ElMessage.warning('请填写完整评价内容和评分')
    return
  }
  // 模拟 API 提交
  // await submitBookReview(reviewForm)

  reviewDialogVisible.value = false
  ElMessage.success('评价提交成功！')
}

// methods
const goToReviewPage = (bookId) => {
  router.push({ name: 'BookReview', params: { id: bookId } })
}
</script>

<style scoped>
.reader-book-list {
  padding: 20px;
}

.book-card {
  height: 100%;
  display: flex;
  flex-direction: column;
  transition: transform 0.2s;
}
.book-card:hover {
  transform: translateY(-2px);
}

.card-wrapper {
  display: flex;
  flex-direction: column;
  height: 100%;
}

.book-cover {
  width: 100%;
  aspect-ratio: 2 / 3;
  object-fit: cover;
  border-radius: 4px;
}

.book-info {
  padding: 8px 4px;
  flex-grow: 1;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
}

.book-title,
.book-author {
  font-size: 12px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.book-title {
  font-weight: bold;
  margin-bottom: 4px;
}

.book-actions {
  margin-top: auto;
  display: flex;
  justify-content: space-between;
  gap: 8px;
  padding-top: 8px;
}

.favorite-button,
.review-button {
  flex: 1;
  font-size: 12px;
  padding: 4px 0;
}
</style>
