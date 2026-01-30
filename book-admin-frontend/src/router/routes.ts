// 或者直接使用默认导入来避免错误
import * as VueRouter from 'vue-router'
import Login from '@/views/login/Login.vue'
import Dashboard from '@/views/dashboard/Index.vue'
import Layout from '@/views/layout/Layout.vue'
import BookDetail from '@/views/book/BookDetail.vue'

const routes: VueRouter.RouteRecordRaw[] = [
  {
    path: '/login',
    component: Login, // 登录页面不需要 Layout
  },
  {
    path: '/403',
    name: 'NoPermission',
    component: () => import('@/views/error/403.vue'),
    meta: { title: '无权限' },
  },
  {
    path: '/',
    component: Layout, // 需要布局的页面
    redirect: '/dashboard', // 默认子路由
    children: [
      {
        path: '/dashboard',
        component: Dashboard,
        meta: { title: '首页', icon: 'Platform' },
      },
      {
        path: '/profile',
        name: 'Profile',
        component: () => import('@/views/layout/Profile.vue'),
        meta: { title: '个人信息' },
      },
      // router/index.ts 或 routes.js
      {
        path: '/favorites',
        name: 'Favorites',
        component: () => import('@/views/layout/Favorites.vue'),
      },
      {
        path: '/book/bookDetail/:id',
        name: 'BookDetail',
        component: BookDetail,
      },
      // 在 router/index.js 中添加
      {
        path: '/book/:id/review',
        name: 'BookReview',
        component: () => import('@/views/book/BookReviewPage.vue'),
      },
      {
        path: '/notice/:id',
        name: 'NoticeDetail',
        component: () => import('@/views/dashboard/components/NoticeDetail.vue'),
        meta: { title: '公告详情' },
      },

      // 可以添加更多需要在 Layout 中展示的页面
    ],
  },
  // 其他路由...
]

export default routes
