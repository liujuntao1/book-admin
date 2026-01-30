<!-- eslint-disable vue/multi-word-component-names -->
<template>
  <div class="error-page">
    <el-empty description="404 页面不存在">
      <el-button type="primary" @click="$router.push('/')">返回首页</el-button>
    </el-empty>
  </div>
</template>

<script setup lang="ts"></script>

<style scoped>
.error-page {
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>
