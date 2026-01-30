import request from '@/utils/request'

export function getCategoryList(params) {
  return request.get('/api/book-categories', { params })
}

export function addCategory(data) {
  return request.post('/api/book-categories', data)
}

export function updateCategory(data) {
  return request.put('/api/book-categories', data)
}

export function deleteCategory(id: number) {
  return request.delete(`/api/book-categories/${id}`)
}
