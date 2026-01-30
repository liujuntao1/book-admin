import { defineAsyncComponent } from 'vue'
import * as VueRouter from 'vue-router'

const modules = import.meta.glob('@/views/**/*.vue')

function loadView(component: string) {
  if (component === 'Layout') {
    return modules['/src/views/layout/Layout.vue']
  }
  const path = `/src/views${component}.vue`
  return modules[path]
}

export function generateRoutes(menuList: any[]): VueRouter.RouteRecordRaw[] {
  const res: VueRouter.RouteRecordRaw[] = []

  for (const item of menuList) {
    const route: VueRouter.RouteRecordRaw = {
      path: item.path,
      name: item.name,
      meta: { title: item.title, icon: item.icon },
    }

    if (item.component === 'Layout') {
      route.component = loadView('Layout')
      route.redirect = item.redirect || (item.children?.[0]?.path ?? '')
      if (item.children && item.children.length > 0) {
        route.children = generateRoutes(item.children)
      }
    } else {
      route.component = loadView(item.component)
    }

    res.push(route)
  }

  return res
}
