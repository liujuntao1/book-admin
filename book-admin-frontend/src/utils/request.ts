import axios from 'axios'
import {ElMessage} from 'element-plus'
import { useUserStore } from '@/stores/user'


// 创建 Axios 实例
const request = axios.create({
  baseURL: import.meta.env.VITE_API_URL, // 从环境变量中获取 API 基础 URL
  timeout: 10000,// 设置请求超时时间
  withCredentials: true // ✅ 关键：允许跨域发送 Cookie
})

// 请求拦截器：在请求头中添加 Authorization token
request.interceptors.request.use(
  config => {
    // 从 localStorage 中获取 token
    const token = localStorage.getItem('token')
    // 如果 token 存在，则添加到请求头中
    if (token) {
      config.headers['Authorization'] = token
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)
// 响应拦截器：处理响应数据
request.interceptors.response.use(
  response => {
    // 获取用户 store（注意这应该在 setup 外部调用前确保 pinia 初始化完成）
const userStore = useUserStore()
    const res = response.data
    if (res.code !== 200) {
      ElMessage.error(res.msg || '请求出错')
      // 判断是否包含 "token 无效"
      if (res.msg && res.msg.includes('token 无效')) {
        userStore.logout()
      }
      return Promise.reject(res)
    }
    return res
  },
  error => {
    // 处理网络异常
    if (error.response) {
      const status = error.response.status
      if (status === 401) {
        ElMessage.error('未授权，请登录')
      } else if (status === 403) {
        ElMessage.error('禁止访问')
      } else if (status === 404) {
        ElMessage.error('请求地址不存在')
      } else {
        ElMessage.error(error.message || '请求失败')
      }
    } else if (error.request) {
      // 请求已发出，但没有响应
      ElMessage.error('网络异常，请检查网络连接')
    } else {
      // 发生了错误，可能是请求配置错误
      ElMessage.error('请求配置错误')
    }
    return Promise.reject(error)
  }
)


export default request
