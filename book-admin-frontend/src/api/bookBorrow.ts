import request from '@/utils/request'

export const apply = (data) => {
  return request.post('/api/book/borrow/apply', data)
}

export const approve = (borrowId) => {
  return request.post(`/api/book/borrow/approve/${borrowId}`)
}

export const reject = (borrowId, reason) => {
  return request.post(`/api/book/borrow/reject/${borrowId}?reason=${encodeURIComponent(reason)}`)
}

export const auditList = (params: any) => request.get('/api/book/borrow/auditList', { params })
export const borrowList = (params: any) => request.get('/api/book/borrow/borrowList', { params })
export const returnBook = (borrowId: number) => {
  return request.post(`/api/book/borrow/return/${borrowId}`)
}

export const isBeingBorrowed = (params: any) => request.get('/api/book/borrow/isBeingBorrowed', { params })