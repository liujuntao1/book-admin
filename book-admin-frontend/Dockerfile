# 使用官方 Nginx 镜像
FROM nginx:alpine

# 复制打包好的前端文件到 nginx 的默认静态目录
COPY dist/ /usr/share/nginx/html

# 可选：复制自定义 nginx 配置（如果有）
COPY nginx.conf /etc/nginx/conf.d/default.conf

# 开放端口
EXPOSE 80

# 启动 nginx
CMD ["nginx", "-g", "daemon off;"]
