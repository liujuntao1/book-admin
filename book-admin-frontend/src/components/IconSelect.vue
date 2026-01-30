<template>
  <div class="icon-select">
    <el-popover placement="bottom-start" width="400" trigger="click">
      <template #reference>
        <el-input v-model="localValue" readonly placeholder="请选择图标">
          <template #prefix>
            <!-- 显示选择的图标 -->
            <component :is="localValue" v-if="localValue" />
          </template>
          <template #suffix>
            <el-icon v-if="localValue" @click.stop="clearIcon" class="clear-icon">
              <CircleClose />
            </el-icon>
          </template>
        </el-input>
      </template>

      <div class="icon-grid">
        <el-tooltip
          v-for="icon in iconList"
          :key="icon"
          :content="icon"
          placement="top"
        >
          <component
            :is="icon"
            class="icon-item"
            @click="selectIcon(icon)"
          />
        </el-tooltip>
      </div>
    </el-popover>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'
import * as Icons from '@element-plus/icons-vue'
import { CircleClose } from '@element-plus/icons-vue'

// 接收 props，获取 v-model 传入的 modelValue
const props = defineProps({
  modelValue: String
})

const emit = defineEmits(['update:modelValue'])

const iconList = Object.keys(Icons)
const localValue = ref('')

// 监听 localValue 和 modelValue 变化，确保数据双向绑定
watch(
  () => localValue.value,
  (val) => emit('update:modelValue', val)
)

watch(
  () => props.modelValue,  // 监听 props.modelValue
  (val) => {
    localValue.value = val
  },
  { immediate: true }
)

const selectIcon = (iconName) => {
  localValue.value = iconName
}

const clearIcon = () => {
  localValue.value = ''
}
</script>

<style scoped>
.icon-grid {
  display: grid;
  grid-template-columns: repeat(6, 1fr);
  gap: 10px;
  max-height: 200px;
  overflow-y: auto;
}
.icon-item {
  font-size: 20px;
  cursor: pointer;
  transition: transform 0.2s;
}
.icon-item:hover {
  transform: scale(1.2);
}
.clear-icon {
  cursor: pointer;
  color: #ccc;
}
.clear-icon:hover {
  color: #f56c6c;
}
</style>
