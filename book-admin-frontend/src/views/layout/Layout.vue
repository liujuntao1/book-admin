<template>
  <!-- 主容器 -->
  <el-container style="width: 100%; height: 100vh">
    <!-- 头部 -->
    <el-header class="el-header-custom">
      <div class="header">
        <img src="@/assets/logo.png" alt="logo" class="logo" />
        <h3 class="header-title">图书管理系统</h3>
      </div>
      <!-- 右侧用户信息 -->
      <UserDropdown />
    </el-header>

    <el-container>
      <!-- 侧边栏 -->
      <el-aside width="200px" style="background-color: #f4f4f4; height: 100%; padding: 10px">
        <Menu />
      </el-aside>

      <!-- 主体内容区域 -->
      <el-main style="padding: 20px; overflow: auto">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import Menu from '@/views/layout/Menu.vue'
import { useUserStore } from '@/stores/user'
import UserDropdown from '@/views/layout/UserDropdown.vue'

const userStore = useUserStore()

onMounted(() => {
  userStore.loadLocalCache() // 组件挂载时确保从 localStorage 恢复
})
</script>

<style scoped>
.el-header-custom {
  background-color: #409eff;
  color: white;
  padding: 10px 20px;
  display: flex;
  justify-content: space-between; /* 左右布局 */
  align-items: center;
}
.header {
  display: flex;
  align-items: center;
}
.logo {
  width: 32px;
  height: 32px;
  margin-right: 10px;
}

.header-title {
  font-size: 20px;
  font-weight: bold;
}
</style>
