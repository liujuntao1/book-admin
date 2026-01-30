# 基础镜像：使用轻量级的 JDK 17 镜像
FROM eclipse-temurin:17-jdk-alpine

# 设置工作目录
WORKDIR /app

# 复制本地打包好的 jar 文件
COPY target/*.jar app.jar

# 启动应用（可设置 JAVA_OPTS 环境变量）
ENV JAVA_OPTS=""
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
