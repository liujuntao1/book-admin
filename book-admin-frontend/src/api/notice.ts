import request from '@/utils/request'

// 获取公告列表（分页）
export function getNoticeList(params: { page: number; size: number }) {
  return request({
    url: '/api/notices',
    method: 'get',
    params,
  })
}
// 根据id获取数据
export function getNoticeById(id) {
  return request({
    url: `/api/notices/${id}`,
    method: 'get',
  })
}
// 创建公告
export function createNotice(data: any) {
  return request({
    url: '/api/notices',
    method: 'post',
    data,
  })
}

// 更新公告
export function updateNotice(data: any) {
  return request({
    url: `/api/notices`,
    method: 'put',
    data,
  })
}

// 删除公告
export function deleteNotice(id: number) {
  return request({
    url: `/api/notices/${id}`,
    method: 'delete',
  })
}
