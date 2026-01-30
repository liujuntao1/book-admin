import request from '@/utils/request'

// 获取用户列表
export const getUsers = (page = 1, size = 10) => {
  return request({
    url: '/api/users',
    method: 'get',
    params: {
      page,
      size
    }
  })
}

// 获取用户详情
export const getUserById = (id: number) => {
  return request({
    url: `/api/users/${id}`,
    method: 'get'
  })
}

// 创建用户
export const createUser = (data: any) => {
  return request({
    url: '/api/users',
    method: 'post',
    data
  })
}

// 更新用户
export const updateUser = (data: any) => {
  return request({
    url: '/api/users',
    method: 'put',
    data
  })
}

// 删除用户
export const deleteUser = (id: number) => {
  return request({
    url: `/api/users/${id}`,
    method: 'delete'
  })
}

// 修改密码
export const updatePwd = (data) => {
  return request({
    url: '/api/users/updatePwd',
    method: 'post',
    data
  })
}

// 给用户分配角色
export const assignRoles = (id: number,data:any) => {
  return request({
    url: `/api/role-permissions/user/${id}/roles`,
    method: 'post',
    data
  })
}
//查询用户拥有的角色ID列表
export const getRolesByUserId = (id: number) => {
  return request({
    url: `/api/role-permissions/user/${id}/roles`,
    method: 'get',
  })
}

export const getLoginUserInfo = () => {
  return request.get('/api/users/getLoginUserInfo')
}