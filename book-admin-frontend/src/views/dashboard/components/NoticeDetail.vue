<template>
  <div class="notice-detail">
    <!-- 返回按钮 -->
    <div class="back-button">
      <el-button type="text" @click="goBack">
        <el-icon><ArrowLeft /></el-icon>
        返回
      </el-button>
    </div>

    <el-card>
      <h2 style="margin-bottom: 10px">{{ notice.title }}</h2>
      <div style="color: #999; font-size: 14px; margin-bottom: 20px">
        发布时间：{{ formatTime(notice.publishTime) }}
      </div>

      <el-image
        v-if="notice.coverUrl"
        :src="notice.coverUrl"
        style="width: 200px; height: 300px; object-fit: contain; margin-bottom: 20px"
        :preview-src-list="[notice.coverUrl]"
        preview-teleported
      />

      <div style="white-space: pre-wrap; line-height: 1.6; font-size: 16px">
        {{ notice.content }}
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter,useRoute } from 'vue-router' // 导入 vue-router
import { getNoticeById } from '@/api/notice'
import dayjs from 'dayjs'
import { ArrowLeft } from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()
const notice = ref({})

const fetchNoticeDetail = async () => {
  const { data } = await getNoticeById(route.params.id)
  notice.value = data
}

const goBack = () => {
  router.push('/dashboard') // 默认回退路径
}

const formatTime = (time) => {
  return dayjs(time).format('YYYY-MM-DD HH:mm')
}

onMounted(fetchNoticeDetail)
</script>

<style scoped>
.notice-detail {
  padding: 20px;
}
.back-button {
  margin-bottom: 10px;
}
</style>
