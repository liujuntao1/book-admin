<template>
  <div>
    <h2 style="margin-bottom: 20px">我的收藏</h2>
    <el-row :gutter="20">
      <el-col :span="3" v-for="book in favoriteBooks" :key="book.id">
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
          </div>
        </el-card>
      </el-col>
    </el-row>

    <div style="margin-top: 20px; text-align: center">
      <el-pagination
        layout="prev, pager, next"
        :current-page="queryParams.page"
        :page-size="queryParams.size"
        :total="total"
        @current-change="handlePageChange"
      />
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted } from 'vue'
import { queryFavoritedList } from '@/api/book'
import { useNavigation } from '@/utils/navigation'

const favoriteBooks = ref([])
const total = ref(0)

const queryParams = ref({
  page: 1,
  size: 8,
})

const getFavoriteBooks = async () => {
  const res = await queryFavoritedList({ page: 1, size: 10 })
  favoriteBooks.value = res.data.records
  total.value = res.data.total
}

const handlePageChange = (newPage) => {
  queryParams.value.page = newPage
  getFavoriteBooks()
}

// 跳转到图书详情页面
const { pushBookDetail } = useNavigation()
const goToBookDetail = (bookId) => {
  pushBookDetail(bookId)
}

onMounted(() => {
  getFavoriteBooks()
})
</script>
<style scoped>
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
</style>
