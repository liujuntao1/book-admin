<template>
  <el-menu :default-active="$route.path" router>
    <template v-for="item in permissionList" :key="item.id">
      <MenuItem :item="item"/>
    </template>
  </el-menu>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import MenuItem from './MenuItem.vue'

const permissionList = ref<any[]>([])

onMounted(() => {
  const permission = localStorage.getItem('permission')
  if (permission) {
    permissionList.value = JSON.parse(permission)
  }
})
</script>

<style scoped>
.el-menu {
  width: 100%;
  height: 100%;
  background-color: #f4f4f4;
}

.el-menu-item {
  text-align: center;
}

.el-menu-item.is-active {
  background-color: #409eff;
  color: white;
}
</style>
