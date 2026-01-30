<template>
  <div class="user-dropdown">
    <el-dropdown trigger="hover">
      <span class="dropdown-link">
        <el-avatar :src="userStore.userInfo?.avatar" size="small" class="avatar">
          {{ userStore.userInfo?.username?.charAt(0).toUpperCase() || 'U' }}
        </el-avatar>
        <span class="username">{{ userStore.userInfo?.username || '用户' }}</span>
        <el-icon><arrow-down /></el-icon>
      </span>
      <template #dropdown>
        <el-dropdown-menu>
          <el-dropdown-item disabled>
            当前用户：{{ userStore.userInfo?.username || '用户' }}
          </el-dropdown-item>
          <el-dropdown-item @click="goToProfile">个人信息</el-dropdown-item>
          <el-dropdown-item @click="goToFavorites">我的收藏</el-dropdown-item>
          <el-dropdown-item divided @click="logout">退出登录</el-dropdown-item>
        </el-dropdown-menu>
      </template>
    </el-dropdown>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()

const logout = () => {
  userStore.logout()
  router.push('/login')
}

const goToProfile = () => {
  router.push('/profile')
}

function goToFavorites() {
  router.push('/favorites')
}
</script>

<style scoped>
.user-dropdown {
  display: flex;
  align-items: center;
  justify-content: flex-end;
}

.dropdown-link {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 6px 10px;
  border-radius: 6px;
  transition: background-color 0.2s;
}

.dropdown-link:hover {
  background-color: #f5f7fa;
}

.avatar {
  background-color: #409eff;
}

.username {
  font-size: 14px;
  color: #333;
}
</style>
