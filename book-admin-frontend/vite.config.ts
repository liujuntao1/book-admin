import { fileURLToPath, URL } from 'node:url'
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueJsx from '@vitejs/plugin-vue-jsx'
import vueDevTools from 'vite-plugin-vue-devtools'

// https://vite.dev/config/
export default defineConfig({
  plugins: [vue(), vueJsx(), vueDevTools()],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url)),
    },
  },
  // server: {
  //   host: 'localhost',
  //   port: 5173,
  //   proxy: {
  //     // 代理 /api 前缀的请求
  //     '/api': {
  //       target: 'http://localhost:8080', // 后端地址
  //       changeOrigin: true, // 是否改变请求源（用于处理 CORS）
  //       // rewrite: (path) => path.replace(/^\/api/, ''), // 可选：去掉 /api 前缀（根据后端实际情况）
  //     },
  //   },
  // },
  base: '/', // 默认是根路径，部署到子目录时需要修改
})
