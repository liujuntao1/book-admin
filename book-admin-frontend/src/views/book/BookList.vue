<template>
  <div class="p-4">
    <el-card>
      <el-form :model="queryParams" inline @submit.prevent>
        <!-- 基本查询条件 -->
        <el-form-item label="书名">
          <el-input v-model="queryParams.title" placeholder="请输入书名" clearable />
        </el-form-item>
        <el-form-item label="分类" style="width: 300px">
          <el-select v-model="queryParams.categoryId" placeholder="请选择分类" filterable>
            <el-option
              v-for="item in categoryOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>

        <!-- 高级查询条件（可展开） -->
        <el-form-item label="作者" v-show="isExpanded">
          <el-input v-model="queryParams.author" placeholder="请输入作者" clearable />
        </el-form-item>
        <el-form-item label="出版社" v-show="isExpanded">
          <el-input v-model="queryParams.publisher" placeholder="请输入出版社" clearable />
        </el-form-item>
        <el-form-item label="ISBN" v-show="isExpanded">
          <el-input v-model="queryParams.isbn" placeholder="请输入ISBN" clearable />
        </el-form-item>
        <el-form-item label="出版日期" v-show="isExpanded">
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

        <!-- 操作按钮 -->
        <el-form-item>
          <el-button type="primary" @click="fetchBooks">查询</el-button>
          <el-button @click="resetForm">重置</el-button>
          <el-button type="text" @click="isExpanded = !isExpanded">
            {{ isExpanded ? '收起' : '展开' }}
            <el-icon style="margin-left: 4px">
              <component :is="isExpanded ? 'ArrowUp' : 'ArrowDown'" />
            </el-icon>
          </el-button>
        </el-form-item>
      </el-form>

      <!-- 新增按钮 -->
      <el-button type="primary" @click="openDialog()">新增图书</el-button>
    </el-card>

    <el-card class="mt-4">
      <el-table :data="books" style="width: 100%" stripe>
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
        <el-table-column prop="title" label="书名" />
        <el-table-column prop="author" label="作者" />
        <el-table-column prop="isbn" label="ISBN" />
        <el-table-column prop="publisher" label="出版社" />
        <el-table-column prop="publishTime" label="出版日期" />
        <el-table-column prop="stock" label="库存数量" />
        <!-- 新增：定价 -->
        <el-table-column prop="price" label="定价" />
        <el-table-column prop="location" label="位置" />

        <el-table-column label="分类">
          <template #default="scope">
            {{ getCategoryName(scope.row.categoryId) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="300" align="center">
          <template #default="scope">
            <el-space>
              <el-button size="small" type="info" @click="viewDetail(scope.row)">详情</el-button>
              <el-button size="small" @click="openDialog(scope.row)">编辑</el-button>
              <el-button size="small" type="danger" @click="handleDelete(scope.row.id)"
                >删除
              </el-button>

              <el-dropdown @command="(command) => handleMoreCommand(command, scope.row)">
                <el-button size="small" type="primary">
                  更多
                  <el-icon>
                    <arrow-down />
                  </el-icon>
                </el-button>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item command="viewBorrow">查看借阅记录</el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </el-space>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        layout="total, sizes, prev, pager, next"
        :total="total"
        :page-size="pageSize"
        :page-sizes="[5, 10, 50, 100]"
        @size-change="handleSizeChange"
        @current-change="handlePageChange"
        class="mt-4"
      />
    </el-card>
    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑图书' : '新增图书'" width="800px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="书名">
          <el-input v-model="form.title" />
        </el-form-item>

        <el-form-item label="作者">
          <el-input v-model="form.author" />
        </el-form-item>

        <el-form-item label="出版社">
          <el-input v-model="form.publisher" />
        </el-form-item>

        <el-form-item label="ISBN">
          <el-input v-model="form.isbn" />
        </el-form-item>

        <el-form-item label="出版日期">
          <el-date-picker v-model="form.publishTime" type="date" value-format="YYYY-MM-DD" />
        </el-form-item>

        <el-form-item label="分类" prop="categoryId">
          <el-select v-model="form.categoryId" placeholder="请选择分类" filterable>
            <el-option
              v-for="item in categoryOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="图书封面">
          <el-upload :show-file-list="false" accept="image/*" :before-upload="handleImageUpload">
            <el-button type="primary">上传图片</el-button>
            <div v-if="form.coverUrl" style="margin-top: 10px">
              <img :src="form.coverUrl" style="width: 100px; height: 140px; object-fit: cover" />
            </div>
          </el-upload>
        </el-form-item>
        <el-form-item label="库存数量">
          <el-input v-model="form.stock" type="number" placeholder="请输入库存数量" />
        </el-form-item>
        <el-form-item label="定价">
          <el-input v-model="form.price" type="number" placeholder="请输入价格" />
        </el-form-item>
        <el-form-item label="位置">
          <el-input v-model="form.location" />
        </el-form-item>
        <!-- 新增：图书简介 -->
        <el-form-item label="简介">
          <el-input
            v-model="form.description"
            type="textarea"
            rows="4"
            placeholder="请输入图书简介"
          />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">保存</el-button>
      </template>
    </el-dialog>

    <!-- 查看详情弹窗 -->
    <el-dialog v-model="detailDialogVisible" title="图书详情" width="1000px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="封面">
          <el-image
            v-if="selectedBook.coverUrl"
            :src="selectedBook.coverUrl"
            style="width: 100px; height: 140px; object-fit: cover"
          />
        </el-descriptions-item>
        <el-descriptions-item label="书名">{{ selectedBook.title }}</el-descriptions-item>
        <el-descriptions-item label="作者">{{ selectedBook.author }}</el-descriptions-item>
        <el-descriptions-item label="ISBN">{{ selectedBook.isbn }}</el-descriptions-item>
        <el-descriptions-item label="出版社">{{ selectedBook.publisher }}</el-descriptions-item>
        <el-descriptions-item label="出版日期">{{ selectedBook.publishTime }}</el-descriptions-item>
        <el-descriptions-item label="库存">{{ selectedBook.stock }}</el-descriptions-item>
        <el-descriptions-item label="定价">{{ selectedBook.price }}</el-descriptions-item>
        <el-descriptions-item label="分类"
          >{{ getCategoryName(selectedBook.categoryId) }}
        </el-descriptions-item>
        <el-descriptions-item label="简介" :span="2"
          >{{ selectedBook.description }}
        </el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <el-button @click="detailDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>
    <!-- 展示借阅记录 -->
    <el-dialog v-model="borrowDialogVisible" title="借阅记录" width="700px">
      <el-table :data="borrowRecords" style="width: 100%">
        <el-table-column prop="userName" label="借阅人" />
        <el-table-column prop="borrowDate" label="借出时间" />
        <el-table-column prop="returnDate" label="归还时间" />
        <el-table-column prop="status" label="状态">
          <template #default="{ row }">
            <el-tag :type="statusTypeMap[row.status]">
              {{ statusMap[row.status] }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>
      <template #footer>
        <el-button @click="borrowDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getBookList, deleteBook, createBook, updateBook, getBorrowListByBookId } from '@/api/book'
import type { Book } from '@/types/book'
import { ArrowDown } from '@element-plus/icons-vue'
import { ElMessageBox, ElMessage } from 'element-plus'
import { getCategoryList } from '@/api/bookCategory'

const isExpanded = ref(false)
const statusMap = {
  0: '申请中',
  1: '已借出',
  2: '已拒绝',
  3: '已归还',
}

const statusTypeMap = {
  0: 'info',
  1: 'success',
  2: 'danger',
  3: 'info',
}

const queryParams = ref({
  title: '',
  author: '',
  publisher: '',
  isbn: '',
  dateRange: [],
  categoryId: null,
  querySource: 1,
})
const books = ref<Book[]>([])
const total = ref(0)
const pageSize = ref(5)
const currentPage = ref(1)

const dialogVisible = ref(false)
const form = ref<Book>({
  title: '',
  author: '',
  isbn: '',
  publishTime: '',
  category: '',
})

const fetchBooks = async () => {
  try {
    const size = pageSize.value
    const page = currentPage.value
    const { title, author, publisher, isbn, categoryId, dateRange } = queryParams.value
    const [startTime, endTime] = dateRange || []
    const { data } = await getBookList({
      title,
      author,
      publisher,
      isbn,
      categoryId,
      startTime,
      endTime,
      size,
      page,
    })
    books.value = data.records
    total.value = data.total
  } catch (e) {
    // ElMessage.error('日志加载失败')
  }
}
const resetForm = () => {
  queryParams.value = {
    title: '',
    author: '',
    publisher: '',
    isbn: '',
    dateRange: [],
    categoryId: null,
    querySource: 1,
  }
  fetchBooks()
}

const handlePageChange = (page: number) => {
  currentPage.value = page
  fetchBooks()
}
const handleSizeChange = (newSize) => {
  pageSize.value = newSize
  fetchBooks()
}

const openDialog = (book: Book | null = null) => {
  if (book) {
    form.value = { ...book }
  } else {
    form.value = {
      title: '',
      author: '',
      isbn: '',
      publishTime: '',
      category: '',
    }
  }
  dialogVisible.value = true
}

const handleSubmit = async () => {
  if (form.value.id) {
    const res = await updateBook(form.value)
    if (res.code == 200) {
      ElMessage.success('修改成功')
    } else {
      ElMessage.error('修改失败')
    }
  } else {
    const res = await createBook(form.value)
    if (res.code == 200) {
      ElMessage.success('新增成功')
    } else {
      ElMessage.error('新增失败')
    }
  }
  dialogVisible.value = false
  fetchBooks()
}

const handleDelete = async (id: number) => {
  // 弹出确认框
  ElMessageBox.confirm('您确定要删除这本书吗?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  })
    .then(async () => {
      // 用户点击确定后执行删除
      const res = await deleteBook(id)
      if (res.code == 200) {
        ElMessage.success('删除成功')
      } else {
        ElMessage.error('删除失败')
      }
      // 删除成功后刷新列表
      fetchBooks()
    })
    .catch(() => {
      // 用户点击取消
      ElMessage.info('删除已取消')
    })
}

fetchBooks()

const categoryOptions = ref([])

const fetchCategoryOptions = async () => {
  const { data } = await getCategoryList({ page: 1, size: 100 }) // 一次取完
  categoryOptions.value = data.records
}

onMounted(() => {
  fetchCategoryOptions()
})
const getCategoryName = (id: number) => {
  const match = categoryOptions.value.find((c) => c.id === id)
  return match ? match.name : '未知'
}
const handleImageUpload = (file) => {
  const reader = new FileReader()
  reader.readAsDataURL(file)
  reader.onload = () => {
    form.value.coverUrl = reader.result // base64 编码
  }
  return false // 阻止自动上传
}

const detailDialogVisible = ref(false)
const selectedBook = ref(null)
const viewDetail = (book) => {
  selectedBook.value = { ...book }
  detailDialogVisible.value = true
}

const borrowDialogVisible = ref(false)
const borrowRecords = ref([])

const viewBorrowRecords = async (bookId: any) => {
  const { data } = await getBorrowListByBookId(bookId, { page: 1, size: 10 })
  borrowRecords.value = data.records
  borrowDialogVisible.value = true
}

const handleMoreCommand = (command, row) => {
  if (command === 'viewBorrow') {
    viewBorrowRecords(row.id)
  }
}
</script>
<style scoped>
.mt-4 {
  margin-top: 1rem;
}
</style>
