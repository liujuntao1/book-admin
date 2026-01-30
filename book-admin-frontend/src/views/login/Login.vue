<template>
  <div class="login-page">
    <div class="login-container">
      <div class="login-header">
        <img src="@/assets/logo.png" alt="Logo" class="logo" />
        <!-- <h2>图书管理系统</h2> -->
      </div>

      <el-card class="login-card">
        <el-form :model="loginForm" label-width="70px" class="login-form">
          <el-form-item
            label="用户名"
            :rules="[{ required: true, message: '请输入用户名', trigger: 'blur' }]"
          >
            <el-input v-model="loginForm.username" placeholder="请输入用户名" clearable />
          </el-form-item>
          <el-form-item
            label="密码"
            :rules="[{ required: true, message: '请输入密码', trigger: 'blur' }]"
          >
            <el-input
              v-model="loginForm.password"
              type="password"
              placeholder="请输入密码"
              clearable
            />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" style="width: 100%" @click="handleLogin">登录</el-button>
          </el-form-item>
        </el-form>
        <div>管理员:admin 密码:123456</div>
        <div>普通用户:zhangsan 密码:123456</div>
      </el-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { loginApi } from '@/api/auth'
import { getLoginUserInfo } from '@/api/users'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()

const loginForm = ref({
  username: '',
  password: '',
})

const handleLogin = async () => {
  try {
    const res = await loginApi(loginForm.value)
    if (res.code === 200) {
      userStore.setUserToken(res.data)
      const userRes = await getLoginUserInfo()
      userStore.setUserInfo(userRes.data)
      router.push('/')
      ElMessage.success('登录成功')
    } else {
      ElMessage.error(res.msg || '登录失败')
    }
  } catch (error) {}
}
</script>

<style scoped>
.login-page {
  width: 100vw;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: url('@/assets/library-bg.jpg') no-repeat center center fixed;
  background-size: cover;
  overflow: hidden; /* 防止内部元素导致滚动条 */
}

.login-container {
  max-height: 100%; /* 限制最大高度 */
  backdrop-filter: blur(8px);
  background-color: rgba(255, 255, 255, 0.9);
  padding: 40px 30px;
  border-radius: 16px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.2);
  text-align: center;
  width: 380px;
  overflow: hidden;
  box-sizing: border-box;
}

.login-header {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 25px;
}

.login-header .logo {
  width: 60px;
  height: 60px;
  margin-bottom: 10px;
}

.login-header h2 {
  font-size: 22px;
  font-weight: 600;
  color: #2c3e50;
}

.login-title {
  font-size: 18px;
  margin-bottom: 20px;
  color: #333;
}
.login-card {
  background-color: transparent !important;
  box-shadow: none !important;
  border: none !important;
}
</style>
