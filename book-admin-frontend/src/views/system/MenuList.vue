<template>
  <div class="p-4">
    <el-card>
      <el-button type="primary" @click="openDialog()">新增菜单</el-button>
    </el-card>

    <el-card class="mt-4">
      <el-table
        :data="menus"
        style="width: 100%"
        row-key="id"
        stripe
        :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
      >
        <el-table-column prop="title" label="标题" />
        <el-table-column prop="name" label="名称" />
        <el-table-column prop="path" label="路径" />
        <el-table-column prop="icon" label="图标" />
        <el-table-column prop="type" label="类型" :formatter="formatType" />

        <el-table-column prop="component" label="组件" />
        <el-table-column prop="createTime" label="创建时间" />
        <el-table-column label="操作" width="200">
          <template #default="scope">
            <el-button size="small" type="primary" @click="openDialog(scope.row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDeleteMenu(scope.row.id)"
              >删除</el-button
            >
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑菜单' : '新增菜单'" width="30%">
      <el-form :model="form" label-width="80px" ref="formRef">
        <el-form-item label="上级菜单">
          <el-tree-select
            v-model="form.parentId"
            :data="menus"
            :props="{ value: 'id', label: 'title', children: 'children' }"
            placeholder="选择上级菜单"
            clearable
            check-strictly
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="标题">
          <el-input v-model="form.title" />
        </el-form-item>
        <el-form-item label="名称">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="路径">
          <el-input v-model="form.path" />
        </el-form-item>
        <el-form-item label="组件">
          <el-input v-model="form.component" />
        </el-form-item>
        <el-form-item label="图标">
          <IconSelect v-model="form.icon" />
        </el-form-item>
        <el-form-item label="类型">
          <el-select v-model="form.type" placeholder="请选择">
            <el-option :value="0" label="目录" />
            <el-option :value="1" label="菜单" />
            <el-option :value="2" label="按钮" />
          </el-select>
        </el-form-item>
        <el-form-item label="权限标识">
          <el-input v-model="form.perms" />
        </el-form-item>
        <el-form-item label="显示">
          <el-switch v-model="form.visible" :active-value="1" :inactive-value="0" />
        </el-form-item>
        <el-form-item label="缓存">
          <el-switch v-model="form.keepAlive" :active-value="1" :inactive-value="0" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="form.sort" :min="0" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getMenuList, createMenu, updateMenu, deleteMenu } from '@/api/menu'
import IconSelect from '@/components/IconSelect.vue'

const menus = ref<Menu[]>([])
const dialogVisible = ref(false)
const form = ref({
  parentId: null,
  name: '',
  path: '',
  component: '',
  icon: '',
  type: 1,
  visible: 1,
  sort: 0,
  perms: '',
  keepAlive: 0,
})
const formRef = ref()

const loadMenus = async () => {
  const res = await getMenuList()
  menus.value = res.data
}

const formatType = (row: any) => {
  switch (row.type) {
    case 0:
      return '目录'
    case 1:
      return '菜单'
    case 2:
      return '按钮'
    default:
      return '未知'
  }
}

const openDialog = (row?) => {
  if (row) {
    form.value = { ...row }
  } else {
    form.value = {
      parentId: null,
      name: '',
      path: '',
      component: '',
      icon: '',
      type: 1,
      visible: 1,
      sort: 0,
      perms: '',
      keepAlive: 0,
    }
  }
  dialogVisible.value = true
}

const submitForm = async () => {
  if (form.value.id) {
    const res = await updateMenu(form.value)
    if (res.code == 200) {
      ElMessage.success('修改成功')
    } else {
      ElMessage.error('修改失败')
    }
  } else {
    const res = await createMenu(form.value)
    if (res.code == 200) {
      ElMessage.success('新增成功')
    } else {
      ElMessage.error('新增失败')
    }
  }
  dialogVisible.value = false
  loadMenus()
}

const handleDeleteMenu = async (id: number) => {
  ElMessageBox.confirm('确认删除该菜单吗？', '提示', { type: 'warning' }).then(async () => {
    await deleteMenu(id)
    ElMessage.success('删除成功')
    loadMenus()
  })
}

onMounted(() => {
  loadMenus()
})
</script>

<style scoped>
.el-dialog .el-form-item {
  margin-bottom: 16px;
}
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
}
.mt-4 {
  margin-top: 1rem;
}
</style>
