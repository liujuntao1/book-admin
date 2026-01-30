import request from '@/utils/request'

export const getBookList = (data: any) => request.post('/api/books/pageList', data)
export const getBorrowListByBookId = (bookId, params: any) =>
  request.get(`/api/books/getBorrowListByBookId/${bookId}`, { params }) //根据图书id查询借阅记录
export const getBookById = (id: number) => request.get(`/api/books/${id}`)
export const createBook = (data: any) => request.post('/api/books', data)
export const updateBook = (data: any) => request.put(`/api/books`, data)
export const deleteBook = (id: number) => request.delete(`/api/books/${id}`)

// 添加收藏
export const addFavorites = (data: any) => request.post('/api/favorites', data)
//取消收藏
export const removeFavorites = (params) => request.delete('/api/favorites', { params })
//获取收藏列表
export const queryFavoritedList = (params) =>
  request.get('/api/favorites/queryFavoritedList', { params })

// 添加评价
export function addReview(data) {
  return request({
    url: '/api/reviews',
    method: 'post',
    data,
  })
}

// 修改评价
export function updateReview(data) {
  return request({
    url: '/api/reviews',
    method: 'put',
    data,
  })
}

// 删除评价
export function deleteReview(id) {
  return request({
    url: `/api/reviews/${id}`,
    method: 'delete',
  })
}

// 获取某本书的评价列表（分页）
export function getReviewsByBookId(bookId, page = 1, size = 10) {
  return request({
    url: `/api/reviews/book/${bookId}`,
    method: 'get',
    params: { page, size },
  })
}
