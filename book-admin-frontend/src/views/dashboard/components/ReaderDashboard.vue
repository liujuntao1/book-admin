<template>
  <el-row :gutter="20">
    <!-- 左侧内容 -->
    <el-col :span="16">
      <el-card header="推荐图书">
        <el-row :gutter="16">
          <el-col
            v-for="book in recommendedBooks"
            :key="book.id"
            :span="4"
            style="margin-bottom: 20px"
          >
            <el-card
              shadow="hover"
              style="height: 300px; display: flex; flex-direction: column"
              @click="goToBookDetail(book.bookId)"
            >
              <el-image
                :src="book.coverUrl"
                style="width: 100%; height: 180px; object-fit: cover"
              />
              <div style="flex: 1; padding-top: 10px">
                <div style="font-weight: bold">{{ book.bookTitle }}</div>
                <div style="color: gray; font-size: 13px">{{ book.bookAuthor }}</div>
              </div>
            </el-card>
          </el-col>
        </el-row>

        <!-- 分页器 -->
        <div style="text-align: center; margin-top: 20px">
          <el-pagination
            layout="total, prev, pager, next"
            :total="rbTotal"
            :page-size="rbPageSize"
            @current-change="handlePageChange"
          />
        </div>
      </el-card>

      <!-- 新书上架 -->
      <el-card header="新书上架" style="margin-top: 20px">
        <el-row :gutter="16">
          <el-col v-for="book in newBooks" :key="book.id" :span="4" style="margin-bottom: 20px">
            <el-card shadow="hover" @click="goToBookDetail(book.bookId)">
              <el-image
                :src="book.coverUrl"
                style="width: 100%; height: 200px; object-fit: cover"
              />
              <div style="margin-top: 10px; font-weight: bold">
                {{ book.bookTitle }}
              </div>
              <div style="color: gray; font-size: 13px">
                {{ book.bookAuthor }}
              </div>
            </el-card>
          </el-col>
        </el-row>
        <!-- 分页器 -->
        <div style="text-align: center; margin-top: 20px">
          <el-pagination
            layout="total, prev, pager, next"
            :total="newBookTotal"
            :page-size="newBookPageSize"
            @current-change="handleNewBookPageChange"
          />
        </div>
      </el-card>
    </el-col>

    <!-- 右侧内容 -->
    <el-col :span="8">
      <!-- 热门图书排行榜 -->
      <el-card header="热门图书排行榜">
        <el-table :data="hotBooks" border style="width: 100%">
          <el-table-column label="排名" type="index" width="60" />
          <el-table-column label="书名" prop="title" />
          <el-table-column label="借阅量" prop="borrowCount" width="80" />
        </el-table>
      </el-card>
      <el-card header="系统公告 / 活动" style="margin-top: 20px">
        <el-timeline>
          <el-timeline-item
            v-for="item in notices"
            :key="item.id"
            :timestamp="formatTime(item.publishTime)"
            placement="top"
          >
            <!-- 标题：可点击跳转详情 -->
            <div
              style="
                font-weight: bold;
                font-size: 16px;
                margin-bottom: 5px;
                color: #409eff;
                cursor: pointer;
              "
              @click="goToNoticeDetail(item.id)"
            >
              {{ item.title }}
            </div>

            <!-- 封面图 -->
            <el-image
              v-if="item.coverUrl"
              :src="item.coverUrl"
              style="width: 100px; max-height: 100px; object-fit: cover; margin-bottom: 10px"
              fit="cover"
              :preview-src-list="[item.coverUrl]"
              preview-teleported
            />

            <!-- 内容限制最多显示 100 字 -->
            <div style="white-space: pre-wrap; color: #666">
              {{ truncateText(item.content, 100) }}
            </div>
          </el-timeline-item>
        </el-timeline>
      </el-card>
    </el-col>
  </el-row>
</template>

<script lang="ts" setup>
import { ref, onMounted } from 'vue'
import { getBookRecommendationList } from '@/api/recommendation'
import { getNoticeList } from '@/api/notice'
import { useRouter } from 'vue-router'
import dayjs from 'dayjs'
import { useNavigation } from '@/utils/navigation'

interface Book {
  id: number
  coverUrl: string
  title: string
  author: string
}

interface Notice {
  id: number
  time: string
  content: string
}

const router = useRouter()
// 数据绑定
const recommendedBooks = ref<Book[]>([])
const rbCurrentPage = ref(1)
const rbPageSize = ref(6)
const rbTotal = ref(0)
const newBookCurrentPage = ref(1)
const newBookPageSize = ref(4)
const newBookTotal = ref(0)
const notices = ref<Notice[]>([])
const hotBooks = ref<Book[]>([])
const newBooks = ref<Book[]>([])

// 数据获取
const fetchRecommendedBooks = async () => {
  const { data } = await getBookRecommendationList({
    page: rbCurrentPage.value,
    size: rbPageSize.value,
    type: 1,
  })
  recommendedBooks.value = data.records
  rbTotal.value = data.total
}
const handlePageChange = (page: number) => {
  rbCurrentPage.value = page
  fetchRecommendedBooks()
}

const fetchNotices = async () => {
  const { data } = await getNoticeList({
    page: 1,
    size: 3,
  })
  notices.value = data.records
}

const fetchHotBooks = () => {
  hotBooks.value = [
    { id: 1, title: '热门书籍1', borrowCount: 500 },
    { id: 2, title: '热门书籍2', borrowCount: 400 },
    // 更多数据...
  ]
}

const fetchNewBooks = async () => {
  const { data } = await getBookRecommendationList({
    page: newBookCurrentPage.value,
    size: newBookPageSize.value,
    type: 2,
  })
  newBooks.value = data.records
  newBookTotal.value = data.total
}
const handleNewBookPageChange = (page: number) => {
  newBookCurrentPage.value = page
  fetchNewBooks()
}

const goToNoticeDetail = (id) => {
  router.push({ name: 'NoticeDetail', params: { id } })
}

const truncateText = (text, maxLength = 100) => {
  if (!text) return ''
  return text.length > maxLength ? text.slice(0, maxLength) + '...' : text
}

const formatTime = (time) => {
  return dayjs(time).format('YYYY-MM-DD')
}


// 跳转到图书详情页面
const { pushBookDetail } = useNavigation()
const goToBookDetail = (bookId) => {
  pushBookDetail(bookId)
}

// 初始化数据
onMounted(() => {
  fetchRecommendedBooks()
  fetchNotices()
  fetchHotBooks()
  fetchNewBooks()
})
</script>
