import request from '@/utils/request'

// 获取推荐图书列表
export const getBookRecommendationList = (params) => {
  return request({
    method: 'GET',
    url: '/api/recommend',
    params,
  })
}

// 新增推荐图书
export const createRecommendation = (data) => {
  return request({
    method: 'POST',
    url: '/api/recommend',
    data,
  })
}

// 更新推荐图书
export const updateRecommendation = (data) => {
  return request({
    method: 'PUT',
    url: '/api/recommend',
    data,
  })
}

// 删除推荐图书
export const deleteRecommendation = (id) => {
  return request({
    method: 'DELETE',
    url: `/api/recommend/${id}`,
  })
}
