package com.example.bookadmin.config;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SaTokenConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册 Sa-Token 拦截器，拦截路径可自行调整
        registry.addInterceptor(new SaInterceptor(handle -> {
            if ("OPTIONS".equalsIgnoreCase(SaHolder.getRequest().getMethod())) {
                return;
            }
            StpUtil.checkLogin();
        })).addPathPatterns("/api/**").excludePathPatterns("/api/auth/**");  // 放行所有认证接口
    }
}


