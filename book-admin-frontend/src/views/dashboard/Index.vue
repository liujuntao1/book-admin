<template>
  <div class="dashboard">
    <AdminDashboard v-if="isAdmin" />
    <ReaderDashboard v-else />
  </div>
</template>


<script setup lang="ts">
import { ref, onMounted } from 'vue'
import AdminDashboard from './components/AdminDashboard.vue'
import ReaderDashboard from './components/ReaderDashboard.vue'

const isAdmin = ref(false)

onMounted(() => {
  const rolesStr = localStorage.getItem('roles')
  if (rolesStr) {
    const roles = JSON.parse(rolesStr) // 解析为数组对象
    const roleCodes = roles.map((r: any) => r.code) // 提取 code 字段数组
    isAdmin.value = roleCodes.includes('Librarian') || roleCodes.includes('PlatformAdministrator')
  }
})
</script>

<style scoped>
.dashboard {
  padding: 20px;
}
.dashboard-header {
  margin-bottom: 20px;
}
.right-align {
  text-align: right;
}
</style>
