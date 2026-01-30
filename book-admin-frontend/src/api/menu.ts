// src/api/menu.ts
import request from '@/utils/request'

/** 获取菜单列表（树形结构） */
export const getMenuList = () => {
  return request.get('/api/menus/tree')
}

/** 新增菜单 */
export const createMenu = (data:any) => {
  return request.post('/api/menus', data)
}

/** 更新菜单 */
export const updateMenu = (data:any) => {
  return request.put(`/api/menus`, data)
}

/** 删除菜单 */
export const deleteMenu = (id: number) => {
  return request.delete(`/api/menus/${id}`)
}
