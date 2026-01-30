<template>
  <div class="profile-page">
    <el-card class="user-card">
      <!-- 顶部头像 -->
      <div class="avatar-wrapper">
        <el-avatar :src="user.avatar" size="large" style="background-color: #409eff">
          {{ user.username?.charAt(0).toUpperCase() || 'U' }}
        </el-avatar>
        <div class="username">{{ user.username }}</div>
      </div>

      <!-- 操作按钮 -->
      <div class="action-buttons">
        <el-button type="primary" size="small" @click="openEditDialog">编辑信息</el-button>
        <el-button type="warning" size="small" @click="pwdDialogVisible = true">修改密码</el-button>
      </div>

      <!-- 详细信息展示 -->
      <el-descriptions
        title="用户信息"
        :column="2"
        border
        label-class-name="label"
        content-class-name="content"
      >
        <el-descriptions-item label="用户名">{{ user.username }}</el-descriptions-item>
        <el-descriptions-item label="真实姓名">{{ user.realname }}</el-descriptions-item>
        <el-descriptions-item label="手机号">{{ user.phone }}</el-descriptions-item>
        <el-descriptions-item label="出生日期">{{ user.birthDate }}</el-descriptions-item>
        <el-descriptions-item label="邮箱">{{ user.email || '-' }}</el-descriptions-item>
        <el-descriptions-item label="性别">{{ user.gender || '-' }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{
          formatDate(user.createTime)
        }}</el-descriptions-item>
        <el-descriptions-item label="更新时间">{{
          formatDate(user.updateTime)
        }}</el-descriptions-item>
      </el-descriptions>
    </el-card>

    <!-- 编辑信息弹窗 -->
    <el-dialog v-model="editDialogVisible" title="编辑个人信息" width="500px">
      <el-form :model="editForm" :rules="editRules" ref="editFormRef" label-width="90px">
        <el-form-item label="真实姓名" prop="realname">
          <el-input v-model="editForm.realname" />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="editForm.phone" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="editForm.email" />
        </el-form-item>
        <el-form-item label="出生日期" prop="birthDate">
          <el-date-picker v-model="editForm.birthDate" type="date" placeholder="选择日期" />
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
          <div v-if="editForm.avatar" class="avatar-preview">
            <img :src="editForm.avatar" alt="头像预览" class="avatar" />
          </div>
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-select v-model="editForm.gender" placeholder="选择性别">
            <el-option label="男" value="男" />
            <el-option label="女" value="女" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitEditForm">保存</el-button>
      </template>
    </el-dialog>

    <!-- 修改密码弹窗 -->
    <el-dialog v-model="pwdDialogVisible" title="修改密码" width="500px">
      <el-form :model="pwdForm" :rules="pwdRules" ref="pwdFormRef" label-width="90px">
        <el-form-item label="旧密码" prop="oldPassword">
          <el-input v-model="pwdForm.oldPassword" type="password" show-password />
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input v-model="pwdForm.newPassword" type="password" show-password />
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input v-model="pwdForm.confirmPassword" type="password" show-password />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="pwdDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitPwdForm">提交</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'
import { updateUser, updatePwd } from '@/api/users'
import { getLoginUserInfo } from '@/api/users'
import dayjs from 'dayjs'
import { useRouter } from 'vue-router'
import { computed } from 'vue'

// 获取用户数据
const userStore = useUserStore()
const user = computed(() => userStore.userInfo || {})
const formatDate = (val) => (val ? dayjs(val).format('YYYY-MM-DD HH:mm:ss') : '-')
const router = useRouter()
// 编辑信息相关
const editDialogVisible = ref(false)
const editFormRef = ref()
const editForm = ref()
const editRules = {
  realname: [{ required: true, message: '请输入真实姓名', trigger: 'blur' }],
  phone: [{ required: true, message: '请输入手机号', trigger: 'blur' }],
  email: [{ type: 'email', message: '邮箱格式不正确', trigger: 'blur' }],
}
const openEditDialog = () => {
  editForm.value = { ...user.value } // 同步最新 user 数据
  editDialogVisible.value = true
}
const submitEditForm = async () => {
  editFormRef.value.validate(async (valid) => {
    if (valid) {
      // birthDate 格式化为 yyyy-MM-dd
      if (editForm.value.birthDate) {
        editForm.value.birthDate = dayjs(editForm.value.birthDate).format('YYYY-MM-DD')
      }
      // TODO: 调用接口保存 editForm.value
      const res = await updateUser(editForm.value)
      if (res.code == 200) {
        const userRes = await getLoginUserInfo()
        userStore.setUserInfo(userRes.data)
        ElMessage.success('保存成功')
      } else {
        ElMessage.error('保存成功')
      }
      editDialogVisible.value = false
    }
  })
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
    editForm.value.avatar = reader.result as string
  }
  if (file.raw) {
    reader.readAsDataURL(file.raw)
  }
}

// 修改密码相关
const pwdDialogVisible = ref(false)
const pwdFormRef = ref()
const pwdForm = ref({
  userId: '',
  oldPassword: '',
  newPassword: '',
  confirmPassword: '',
})
const pwdRules = {
  oldPassword: [{ required: true, message: '请输入旧密码', trigger: 'blur' }],
  newPassword: [{ required: true, message: '请输入新密码', trigger: 'blur' }],
  confirmPassword: [
    { required: true, message: '请确认新密码', trigger: 'blur' },
    {
      validator: (_, value) => {
        if (value !== pwdForm.value.newPassword) {
          return new Error('两次密码不一致')
        }
        return true
      },
      trigger: 'blur',
    },
  ],
}

const submitPwdForm = () => {
  pwdFormRef.value.validate(async (valid) => {
    if (valid) {
      pwdForm.value.userId = user.value.id
      // TODO: 调用修改密码接口
      const res = await updatePwd(pwdForm.value)
      if (res.code == 200) {
        ElMessage.success('密码修改成功')
        userStore.logout()
        router.push('/login')
      } else {
        ElMessage.error('密码修改失败')
      }
      pwdDialogVisible.value = false
    }
  })
}
</script>

<style scoped>
.profile-page {
  background: #f5f7fa;
  min-height: 100vh;
  padding: 40px 20px;
  display: flex;
  justify-content: center;
}

.user-card {
  width: 900px;
  padding: 20px;
}

.avatar-wrapper {
  text-align: center;
  margin-bottom: 20px;
}

.username {
  margin-top: 10px;
  font-size: 18px;
  font-weight: bold;
}

.action-buttons {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 20px;
  gap: 10px;
}

.label {
  background: #f0f2f5;
  font-weight: 500;
}

.content {
  color: #333;
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
</style>
