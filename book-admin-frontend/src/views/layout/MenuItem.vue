<template>
  <!-- 如果有子菜单，则渲染为 el-sub-menu -->
  <el-sub-menu v-if="hasChildren" :index="item.path">
    <template #title>
      <el-icon v-if="iconComponent">
        <component :is="iconComponent" />
      </el-icon>
      <span>{{ item.title }}</span>
    </template>
    <template v-for="child in item.children" :key="child.id">
      <MenuItem :item="child" />
    </template>
  </el-sub-menu>

  <!-- 否则渲染为普通菜单项 -->
  <el-menu-item v-else :index="item.path">
    <el-icon v-if="iconComponent">
      <component :is="iconComponent" />
    </el-icon>
    <span>{{ item.title }}</span>
  </el-menu-item>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import * as ElementPlusIcons from '@element-plus/icons-vue'

const props = defineProps<{
  item: any
}>()

const iconComponent = computed(() => {
  return props.item.icon && ElementPlusIcons[props.item.icon]
    ? ElementPlusIcons[props.item.icon]
    : null
})

const hasChildren = computed(() => {
  return props.item.children && props.item.children.length > 0
})
</script>
