<template>
  <div class="p-4">
    <el-card>
      <el-button type="primary" @click="openCreateDialog">创建角色</el-button>
    </el-card>

    <el-card class="mt-4">
      <!-- 角色表格 -->
      <el-table :data="roles" style="width: 100%" stripe>
        <el-table-column prop="name" label="角色名称" />
        <el-table-column prop="code" label="角色编码" />
        <el-table-column prop="description" label="描述" />
        <el-table-column prop="status" label="状态" :formatter="formatStatus" />
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column prop="updateTime" label="更新时间" width="180" />
        <el-table-column label="操作" fixed="right" width="250">
          <template #default="scope">
            <el-button size="small" @click="editRole(scope.row)">编辑</el-button>
            <el-button size="small" type="danger" @click="removeRole(scope.row.id)">删除</el-button>
            <el-button size="small" type="success" @click="handleAssignMenus(scope.row)"
              >菜单授权</el-button
            >
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :total="total"
        @current-change="fetchRoles"
        layout="total, prev, pager, next, jumper"
      />
    </el-card>
    <!-- 角色创建/编辑对话框 -->
    <el-dialog v-model="dialogVisible" title="角色信息" width="30%">
      <el-form :model="form" label-width="80px" ref="formRef">
        <el-form-item label="名称" prop="name" :rules="[{ required: true, message: '请输入名称' }]">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="编码" prop="code" :rules="[{ required: true, message: '请输入编码' }]">
          <el-input v-model="form.code" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" type="textarea" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status" placeholder="请选择">
            <el-option label="启用" :value="1" />
            <el-option label="禁用" :value="0" />
          </el-select>
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">保存</el-button>
      </template>
    </el-dialog>

    <!-- 授权弹窗 -->
    <el-dialog
      v-model="menuDialogVisible"
      title="菜单授权"
      width="600px"
      @close="handleMenuDialogClose"
    >
      <el-tree
        ref="menuTreeRef"
        :data="menuTree"
        :props="defaultProps"
        show-checkbox
        node-key="id"
        default-expand-all
      />
      <template #footer>
        <el-button @click="menuDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitMenuAssignment">确认授权</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  getRoles,
  createRole,
  updateRole,
  deleteRole,
  getMenusByRoleId,
  assignMenus,
} from '@/api/role'
import { getMenuList } from '@/api/menu' // 模拟或实际接口
const roles = ref<any[]>([])
const total = ref(0)
const pageSize = ref(10)
const currentPage = ref(1)

const dialogVisible = ref(false)
const form = ref({
  id: null,
  name: '',
  code: '',
  description: '',
  status: 1,
})

const formRef = ref()

// 加载数据
const fetchRoles = async () => {
  try {
    const res = await getRoles({ page: currentPage.value, size: pageSize.value })
    roles.value = res.data.records
    total.value = res.data.total
  } catch (err) {
    ElMessage.error('加载角色列表失败')
  }
}

const openCreateDialog = () => {
  form.value = { id: null, name: '', code: '', description: '', status: 1 }
  dialogVisible.value = true
}

const editRole = (row: any) => {
  form.value = { ...row }
  dialogVisible.value = true
}

const removeRole = (id: number) => {
  ElMessageBox.confirm('确认删除该角色？', '提示', {
    confirmButtonText: '删除',
    cancelButtonText: '取消',
    type: 'warning',
  }).then(async () => {
    await deleteRole(id)
    ElMessage.success('删除成功')
    fetchRoles()
  })
}

const submitForm = async () => {
  try {
    const isEdit = !!form.value.id
    if (isEdit) {
      const res = await updateRole(form.value)
      if (res.code == 200) {
        ElMessage.success('修改成功')
      } else {
        ElMessage.error('修改失败')
      }
    } else {
      const res = await createRole(form.value)
      if (res.code == 200) {
        ElMessage.success('新增成功')
      } else {
        ElMessage.error('新增失败')
      }
    }
    dialogVisible.value = false
    fetchRoles()
  } catch (error) {
    ElMessage.error('保存失败')
  }
}

const formatStatus = (row: any) => {
  return row.status === 1 ? '启用' : '禁用'
}
onMounted(() => {
  fetchRoles()
})
const menuDialogVisible = ref(false)
const selectedRoleId = ref(null)
const menuTree = ref([])
const menuTreeRef = ref()

const defaultProps = {
  children: 'children',
  label: 'name',
}

// 打开授权弹窗
const handleAssignMenus = async (role) => {
  selectedRoleId.value = role.id
  menuDialogVisible.value = true

  // 获取菜单树
  const res = await getMenuList()
  menuTree.value = res.data // 你提供的结构可以直接赋值

  // 获取已授权的菜单 ID
  const checkedRes = await getMenusByRoleId(role.id)
  const checkedMenuIds = checkedRes.data

  nextTick(() => {
    menuTreeRef.value.setCheckedKeys(checkedMenuIds)
  })
}

// 提交授权
const submitMenuAssignment = async () => {
  const checkedKeys = menuTreeRef.value.getCheckedKeys()
  await assignMenus(selectedRoleId.value, checkedKeys)
  ElMessage.success('菜单授权成功')
  menuDialogVisible.value = false
}

// 弹窗关闭时清空状态
const handleMenuDialogClose = () => {
  selectedRoleId.value = null
  menuTree.value = []
}
</script>

<style scoped>
.mt-4 {
  margin-top: 1rem;
}
</style>
