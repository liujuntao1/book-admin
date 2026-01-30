<template>
  <div class="review-page">
    <!-- 图书详情区域 -->
    <div class="book-info-card">
      <el-card>
        <el-row>
          <el-col :span="6">
            <el-image :src="book.coverUrl" style="width: 100%" />
          </el-col>
          <el-col :span="18">
            <h2>{{ book.title }}</h2>
            <p>作者：{{ book.author }}</p>
            <p>简介：{{ book.description }}</p>
          </el-col>
        </el-row>
      </el-card>
    </div>

    <!-- 用户评价表单 -->
    <div class="review-form" style="margin-top: 20px">
      <el-card>
        <h3>写下你的评价</h3>
        <el-form :model="form" @submit.native.prevent="submitReview">
          <el-form-item label="评分">
            <el-rate v-model="form.rating" allow-half />
          </el-form-item>
          <el-form-item label="评价内容">
            <el-input type="textarea" v-model="form.content" rows="4" />
          </el-form-item>
          <el-button type="primary" @click="submitReview">提交评价</el-button>
        </el-form>
      </el-card>
    </div>

    <!-- 图书评价列表 -->
    <div class="review-list" style="margin-top: 20px">
      <el-card>
        <h3>其他读者的评价</h3>
        <div
          v-for="review in reviews"
          :key="review.id"
          class="review-item"
          style="margin-bottom: 16px"
        >
          <div style="display: flex; justify-content: space-between; align-items: center">
            <div>
              <p>
                <strong>{{ review.username }}</strong>
              </p>
              <el-rate
                :model-value="review.rating"
                disabled
                show-score
                score-template="{value} 分"
              />
            </div>
            <div style="color: #999; font-size: 12px">
              {{ formatTime(review.createTime) }}
            </div>
          </div>
          <p style="margin-top: 8px">{{ review.content }}</p>
          <el-divider />
        </div>

        <el-pagination
          v-model:current-page="page"
          v-model:page-size="pageSize"
          :total="total"
          layout="prev, pager, next"
          @current-change="fetchReviews"
        />
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getBookById } from '@/api/book'
import { addReview, getReviewsByBookId } from '@/api/book'
import { ElMessage } from 'element-plus'
import dayjs from 'dayjs'
const formatTime = (time) => {
  return dayjs(time).format('YYYY-MM-DD HH:mm')
}
const route = useRoute()
const book = ref({})
const reviews = ref([])
const total = ref(0)
const page = ref(1)
const pageSize = ref(5)

const form = ref({
  content: '',
  rating: 0,
  bookId: route.params.id,
  userId: '', // 从 localStorage 取用户 id
  username: '',
})

// 获取图书详情
const fetchBook = async () => {
  const { data } = await getBookById(route.params.id)
  book.value = data
}

// 获取图书评价
const fetchReviews = async () => {
  const { data } = await getReviewsByBookId(route.params.id, page.value, pageSize.value)
  reviews.value = data.records
  total.value = data.total
}

// 提交评价
const submitReview = async () => {
  const res = await addReview(form.value)
  if (res.code === 200) {
    ElMessage.success('评价成功')
    form.value.content = ''
    form.value.rating = 0
    fetchReviews()
  } else {
    ElMessage.error(res.msg || '提交失败')
  }
}

onMounted(() => {
  const user = JSON.parse(localStorage.getItem('userInfo') || '{}')
  form.value.userId = user.id
  form.value.username = user.username
  fetchBook()
  fetchReviews()
})
</script>

<style scoped>
.review-item {
  padding: 10px 0;
}
</style>
