<template>
  <el-card>
    <template #header>
      <span>图书借阅申请</span>
    </template>

    <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
      <el-form-item label="图书" prop="bookId">
        <el-select v-model="form.bookId" placeholder="请选择图书" @change="handleBookChange">
          <el-option
            v-for="book in bookOptions"
            :key="book.id"
            :label="book.title"
            :value="book.id"
          >
            <!-- 显示封面 + 标题 -->
            <div style="display: flex; align-items: center; height: 60px">
              <el-image
                :src="book.coverUrl"
                style="width: 40px; height: 60px; object-fit: cover; margin-right: 8px"
              />
              <span style="line-height: 60px">{{ book.title }}</span>
            </div>
          </el-option>
        </el-select>
      </el-form-item>

      <!-- 图书详情展示 -->
      <el-form-item v-if="selectedBook" label-width="0">
        <el-descriptions title="图书信息" :column="1" border>
          <el-descriptions-item label="封面">
            <el-image :src="selectedBook.coverUrl" style="width: 80px; height: 120px" fit="cover" />
          </el-descriptions-item>
          <el-descriptions-item label="价格">{{ selectedBook.price }} 元</el-descriptions-item>
          <el-descriptions-item label="库存">{{ selectedBook.stock }}</el-descriptions-item>
          <el-descriptions-item label="简介">{{ selectedBook.description }}</el-descriptions-item>
        </el-descriptions>
      </el-form-item>

      <el-form-item label="申请用户" prop="userId">
        <el-input v-model="form.username" disabled />
      </el-form-item>

      <el-form-item label="用户ID" prop="userId" v-show="false">
        <el-input v-model="form.userId" />
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="submitForm" :disabled="selectedBookStock <= 0"
          >提交申请</el-button
        >
        <el-button @click="resetForm">重置</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getBookList } from '@/api/book'
import { apply } from '@/api/bookBorrow'

const formRef = ref()
const form = ref({
  bookId: '',
  userId: '',
  username: '',
})

const rules = {
  bookId: [{ required: true, message: '请选择图书', trigger: 'blur' }],
  // userId: [{ required: true, message: '请输入用户ID', trigger: 'blur' }],
}

const bookOptions = ref([])

const loadBooks = async () => {
  const { data } = await getBookList({ page: 1, size: 100 })
  bookOptions.value = data.records
}

const submitForm = () => {
  formRef.value.validate(async (valid: boolean) => {
    if (valid) {
      const res = await apply(form.value)
      if (res.code === 200) {
        ElMessage.success('申请成功，等待审批')
        resetForm()
      } else {
        ElMessage.error(res.msg || '提交失败')
      }
    }
  })
}

const resetForm = () => {
  form.value.bookId = ''
  const user = localStorage.getItem('userInfo')
  if (user) {
    form.value.username = JSON.parse(user).username
    form.value.userId = JSON.parse(user).id
  }
  selectedBook.value = null
}

onMounted(() => {
  loadBooks()
  const user = localStorage.getItem('userInfo')
  if (user) {
    form.value.username = JSON.parse(user).username
    form.value.userId = JSON.parse(user).id
  }
})

const selectedBook = ref(null)
const selectedBookStock = ref(0)
const handleBookChange = (bookId) => {
  selectedBook.value = bookOptions.value.find((book) => book.id === bookId) || null
  selectedBookStock.value = selectedBook.value ? selectedBook.value.stock : 0
}
</script>
