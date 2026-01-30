import {createRouter, createWebHistory} from 'vue-router'
import routes from './routes'
import {ElMessage} from 'element-plus'
import {generateRoutes} from './permission'
import {useUserStore} from '@/stores/user'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import getPageTitle from '@/utils/get-page-title'

NProgress.configure({showSpinner: false})

const whiteList: string[] = ['/login']

const router = createRouter({
  history: createWebHistory(),
  routes: [...routes],
})

// 标记是否已添加动态路由
let isDynamicAdded = false

// 兜底路由定义
const notFoundRoute: RouteRecordRaw = {
  path: '/:pathMatch(.*)*',
  name: '404',
  component: () => import('@/views/error/404.vue'),
  meta: {title: '页面未找到'},
}

// 在 addDynamicRoutes 函数最后添加它
async function addDynamicRoutes(menus: any[]) {
  const dynamicRoutes = generateRoutes(menus)
  dynamicRoutes.forEach(route => router.addRoute(route))

  // ✅ 添加 404 兜底路由（注意顺序，最后加）
  router.addRoute(notFoundRoute)

  isDynamicAdded = true
}

router.beforeEach(async (to, from, next) => {
  console.log('to:', to.name)
  console.log('from:', from.name)
  NProgress.start()
  document.title = getPageTitle(to.meta.title)

  const userStore = useUserStore()
  userStore.loadLocalCache()
  const token = userStore.token
  const menus = userStore.menus

  if (token) {
    if (to.path === '/login') {
      NProgress.done()
      return next({path: '/'})
    }

    if (!isDynamicAdded && menus && menus.length > 0) {
      await addDynamicRoutes(menus)
      return next({...to, replace: true})
    }

    return next()
  } else {
    if (whiteList.includes(to.path)) {
      return next()
    } else {
      ElMessage.error('请先登录')
      NProgress.done()
      return next({path: '/login', query: {redirect: to.fullPath}})
    }
  }
})

router.afterEach(() => {
  NProgress.done()
})

export default router
