import request from '@/utils/request'

export const getRoles = (params: { page: number; size: number }) =>
  request.get('/api/roles', { params })
export const createRole = (data: any) => request.post('/api/roles', data)
export const updateRole = (data: any) => request.put(`/api/roles`, data)
export const deleteRole = (id: number) => request.delete(`/api/roles/${id}`)

// 给角色分配菜单权限
export const assignMenus = (id: number, data: any) => {
  return request({
    url: `/api/role-permissions/role/${id}/menus`,
    method: 'post',
    data,
  })
}
//查询角色拥有的菜单ID列表
export const getMenusByRoleId = (id: number) => {
  return request({
    url: `/api/role-permissions/role/${id}/menus`,
    method: 'get',
  })
}
