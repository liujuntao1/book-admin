<template>
  <div class="p-4">
    <el-card>
      <el-button type="primary" @click="openDialog()">新增分类</el-button>
    </el-card>

    <el-card class="mt-4">
      <el-table :data="categories" stripe>
        <el-table-column prop="name" label="分类名称" />
        <el-table-column prop="description" label="分类描述" />
        <el-table-column label="操作" width="180">
          <template #default="scope">
            <el-button size="small" @click="openDialog(scope.row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(scope.row.id)"
              >删除</el-button
            >
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        :total="total"
        :page-size="pageSize"
        @current-change="handlePageChange"
        class="mt-4"
      />
    </el-card>
    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑分类' : '新增分类'" width="500px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="分类名称">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { getCategoryList, addCategory, updateCategory, deleteCategory } from '@/api/bookCategory'
import { ElMessageBox, ElMessage } from 'element-plus'

const categories = ref([])
const total = ref(0)
const pageSize = ref(10)
const currentPage = ref(1)

const dialogVisible = ref(false)
const form = ref({ name: '', description: '', id: null })

const fetchData = async () => {
  const { data } = await getCategoryList({ page: currentPage.value, size: pageSize.value })
  categories.value = data.records
  total.value = data.total
}

const openDialog = (row = null) => {
  if (row) {
    form.value = { ...row }
  } else {
    form.value = { name: '', description: '', id: null }
  }
  dialogVisible.value = true
}

const handleSubmit = async () => {
  if (form.value.id) {
    const res = await updateCategory(form.value)
    if (res.code == 200) {
      ElMessage.success('修改成功')
    } else {
      ElMessage.error('修改失败')
    }
  } else {
    const res = await addCategory(form.value)
    if (res.code == 200) {
      ElMessage.success('新增成功')
    } else {
      ElMessage.error('新增失败')
    }
  }
  dialogVisible.value = false
  fetchData()
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
      const res = await deleteCategory(id)
      if (res.code == 200) {
        ElMessage.success('删除成功')
      } else {
        ElMessage.error('删除失败')
      }
      // 删除成功后刷新列表
      fetchData()
    })
    .catch(() => {
      // 用户点击取消
      ElMessage.info('删除已取消')
    })
}

const handlePageChange = (page: number) => {
  currentPage.value = page
  fetchData()
}

fetchData()
</script>
<style scoped>
.mt-4 {
  margin-top: 1rem;
}
</style>
