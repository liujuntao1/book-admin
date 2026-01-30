// src/api/auth.ts
import request from '@/utils/request'

export interface LoginForm {
  username: string
  password: string
}

export const loginApi = (data: LoginForm) => {
  return request.post('/api/auth/login', data)
}
