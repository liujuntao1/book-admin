import { defineStore } from 'pinia'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: '',
    userInfo: null,
    roles: [],
    menus: [],
  }),
  actions: {
    setUserToken(data: any) {
      this.token = data
      localStorage.setItem('token', data)
    },
    setUserInfo(data: any) {
      this.userInfo = data.user
      this.roles = data.roles
      this.menus = data.menus
      localStorage.setItem('userInfo', JSON.stringify(data.user))
      localStorage.setItem('roles', JSON.stringify(data.roles))
      localStorage.setItem('permission', JSON.stringify(data.menus))
    },
    loadLocalCache() {
      this.token = localStorage.getItem('token') || ''
      this.userInfo = JSON.parse(localStorage.getItem('userInfo') || 'null')
      this.roles = JSON.parse(localStorage.getItem('roles') || '[]')
      this.menus = JSON.parse(localStorage.getItem('permission') || '[]')
    },
    logout() {
      this.token = ''
      this.userInfo = null
      this.roles = []
      this.menus = []
      localStorage.clear()
    },
  },
})
