<template>
  <div class="p-4">
    <el-card>
      <el-button type="primary" @click="openCreateUserDialog">创建用户</el-button>
    </el-card>

    <el-card class="mt-4">
      <!-- 用户列表 -->
      <el-table :data="users" stripe style="width: 100%">
        <!-- 第一列固定宽度 -->
        <el-table-column prop="username" label="用户名" width="180" />
        <el-table-column prop="realname" label="真实姓名" width="180" />

        <!-- 头像列 -->
        <el-table-column label="头像" width="100">
          <template #default="scope">
            <!-- 显示头像，使用 Base64 数据 -->
            <img
              :src="scope.row.avatar"
              alt="头像"
              style="width: 40px; height: 40px; border-radius: 50%"
            />
          </template>
        </el-table-column>

        <!-- 其他列 -->
        <el-table-column prop="phone" label="手机号" />
        <el-table-column prop="birthDate" label="出生日期" />
        <el-table-column prop="createTime" label="创建时间" />
        <el-table-column prop="updateTime" label="更新时间" />

        <!-- 操作列固定在右侧 -->
        <el-table-column label="操作" fixed="right" width="300">
          <template #default="scope">
            <el-button @click="viewUser(scope.row.id)" size="small" type="primary">查看</el-button>
            <el-button @click="editUser(scope.row.id)" size="small" type="warning">编辑</el-button>
            <el-button @click="heandleDeleteUser(scope.row.id)" size="small" type="danger"
              >删除</el-button
            >
            <!-- 授权按钮 -->
            <el-button @click="handleAssignRoles(scope.row.id)" size="small" type="success"
              >授权</el-button
            >
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
        :current-page="currentPage"
        :page-size="pageSize"
        :total="total"
        @current-change="handlePageChange"
        layout="total, prev, pager, next, jumper"
      />
    </el-card>
    <!-- 创建/编辑用户对话框 -->
    <el-dialog v-model="dialogVisible" title="用户信息" width="30%">
      <el-form :model="form" ref="formRef" label-width="80px">
        <el-form-item label="用户名" :rules="[{ required: true, message: '请输入用户名' }]">
          <el-input v-model="form.username" />
        </el-form-item>
        <el-form-item label="真实姓名" :rules="[{ required: true, message: '请输入真实姓名' }]">
          <el-input v-model="form.realname" />
        </el-form-item>
        <el-form-item label="手机号" :rules="[{ required: true, message: '请输入手机号' }]">
          <el-input v-model="form.phone" />
        </el-form-item>

        <el-form-item label="出生日期">
          <el-date-picker v-model="form.birthDate" type="date" placeholder="选择出生日期" />
        </el-form-item>

        <!-- 头像上传部分 -->
        <el-form-item label="头像">
          <el-upload
            class="avatar-uploader"
            action="#"
            :before-upload="handleBeforeUpload"
            :show-file-list="false"
            :on-change="handleAvatarChange"
            :limit="1"
          >
            <el-button size="small" type="primary">点击上传头像</el-button>
          </el-upload>
          <div v-if="form.avatar" class="avatar-preview">
            <img :src="form.avatar" alt="头像预览" class="avatar" />
          </div>
        </el-form-item>
      </el-form>

      <template v-slot:footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="saveUser">保存</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 授权弹窗 -->
    <el-dialog v-model="roleDialogVisible" title="授权角色" width="35%">
      <!-- 穿梭框 -->
      <el-transfer
        v-model="selectedRoles"
        :data="roleList"
        :titles="['角色列表', '已授权角色']"
        :filterable="true"
        :loading="roleListLoading"
        style="width: 100%"
      />
      <template v-slot:footer>
        <span class="dialog-footer">
          <el-button @click="roleDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitRoles">确认授权</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>
<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox, ElNotification } from 'element-plus'
import {
  getUsers,
  getUserById,
  createUser,
  updateUser,
  deleteUser,
  assignRoles,
  getRolesByUserId,
} from '@/api/users'
import { getRoles } from '@/api/role'
import dayjs from 'dayjs'

const users = ref<any[]>([])
const total = ref(0) // 总条数
const currentPage = ref(1) // 当前页
const pageSize = ref(10) // 每页条数

const dialogVisible = ref(false)
const form = ref<any>({
  username: '',
  realname: '',
  phone: '',
  birthDate: '',
  avatar: '',
})
const formRef = ref(null)

onMounted(() => {
  loadUsers(currentPage.value, pageSize.value)
})

// 加载用户列表
const loadUsers = async (page = 1, size = 10) => {
  try {
    const response = await getUsers(page, size)
    users.value = response.data.records // 更新用户列表
    total.value = response.data.total // 总条数，用于分页显示
  } catch (error) {
    ElMessage.error('获取用户列表失败')
  }
}
// 页码变化时触发加载新一页数据
const handlePageChange = (page: number) => {
  currentPage.value = page
  loadUsers(page, pageSize.value)
}

// 打开创建用户对话框
const openCreateUserDialog = () => {
  form.value = { username: '', realname: '', phone: '', birthDate: '', avatar: '' }
  dialogVisible.value = true
}

// 查看用户
const viewUser = async (id: number) => {
  try {
    const response = await getUserById(id)
    ElNotification({
      title: '用户详情',
      message: JSON.stringify(response.data),
      type: 'info',
    })
  } catch (error) {
    ElMessage.error('获取用户详情失败')
  }
}

// 编辑用户
const editUser = async (id: number) => {
  try {
    const response = await getUserById(id)
    form.value = response.data
    dialogVisible.value = true
  } catch (error) {
    ElMessage.error('获取用户信息失败')
  }
}

// 删除用户
const heandleDeleteUser = (id: number) => {
  ElMessageBox.confirm('确认删除该用户吗?', '警告', {
    confirmButtonText: '删除',
    cancelButtonText: '取消',
    type: 'warning',
  })
    .then(async () => {
      try {
        await deleteUser(id)
        ElMessage.success('删除成功')
        loadUsers()
      } catch (error) {
        ElMessage.error('删除失败')
      }
    })
    .catch(() => {
      // 用户取消
    })
}

// 保存用户信息
const saveUser = async () => {
  if (!form.value.username || !form.value.phone) {
    ElMessage.error('请填写完整的用户信息')
    return
  }
  // birthDate 格式化为 yyyy-MM-dd
  if (form.value.birthDate) {
    form.value.birthDate = dayjs(form.value.birthDate).format('YYYY-MM-DD')
  }

  try {
    if (form.value.id) {
      // 更新用户
      const res = await updateUser(form.value)
      if (res.code == 200) {
        ElMessage.success('修改成功')
      } else {
        ElMessage.error('修改失败')
      }
    } else {
      // 创建用户
      const res = await createUser(form.value)
      if (res.code == 200) {
        ElMessage.success('新增成功')
      } else {
        ElMessage.error('新增失败')
      }
    }
  } catch (error) {
    ElMessage.error('保存失败')
  }
  loadUsers()
  dialogVisible.value = false
}

// 上传前处理
const handleBeforeUpload = (file) => {
  const isImage = file.type.startsWith('image')
  if (!isImage) {
    ElMessage.error('请上传图片文件')
  }
  return isImage
}

// 头像上传变化时的处理
const handleAvatarChange = (file) => {
  const reader = new FileReader()
  reader.onloadend = () => {
    // 将图片转为Base64格式
    form.value.avatar = reader.result as string
  }
  if (file.raw) {
    reader.readAsDataURL(file.raw)
  }
}
// 数据
const roleDialogVisible = ref(false) // 控制弹窗显示
const selectedRoles = ref([]) // 选择的角色列表
const roleList = ref([]) // 角色列表
const roleListLoading = ref(false) // 加载角色列表的标志
const selectedUserId = ref('') // 选择的用户ID

// 加载角色列表
const loadAllRoles = async () => {
  if (roleList.value.length) return // 如果角色列表已加载，则不再重复请求
  roleListLoading.value = true
  try {
    const data = await getRoles({ page: 1, size: 100 })
    roleList.value = data.data.records.map((role) => ({
      key: role.id,
      label: role.name,
    }))
  } catch (error) {
    console.error('加载角色列表失败', error)
    ElMessage.error('加载角色列表失败')
  } finally {
    roleListLoading.value = false
  }
}

// 加载已授权角色列表
const loadAuthorizedRoles = async (userId) => {
  try {
    const data = await getRolesByUserId(userId)
    selectedRoles.value = data.data // 更新已授权角色列表
  } catch (error) {
    console.error('加载已授权角色失败', error)
    ElMessage.error('加载已授权角色失败')
  }
}

// 授权角色
const handleAssignRoles = async (userId) => {
  selectedRoles.value = [] // 清空选中的角色
  selectedUserId.value = userId
  roleDialogVisible.value = true
  // 并行加载角色和已授权角色列表
  await Promise.all([loadAllRoles(), loadAuthorizedRoles(userId)])
}

// 提交授权
const submitRoles = async () => {
  const userId = selectedUserId.value // 选择的用户ID
  const rolesToAssign = selectedRoles.value // 获取选择的角色列表
  try {
    await assignRoles(userId, rolesToAssign)
    roleDialogVisible.value = false
    ElMessage.success('角色授权成功')
  } catch (error) {
    console.error('角色授权失败', error)
    ElMessage.error('角色授权失败')
  }
}
</script>

<style scoped>
.dialog-footer {
  display: flex;
  justify-content: space-between;
}
.avatar-uploader {
  display: inline-block;
}

.avatar-preview {
  margin-top: 10px;
  width: 100px;
  height: 100px;
  border-radius: 50%;
  overflow: hidden;
}

.avatar-preview img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
.mt-4 {
  margin-top: 1rem;
}
</style>
