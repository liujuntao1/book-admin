package com.example.bookadmin.config.aspect;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import com.alibaba.fastjson.JSON;
import com.example.bookadmin.common.R;
import com.example.bookadmin.entity.SysLog;
import com.example.bookadmin.entity.User;
import com.example.bookadmin.service.SysLogService;
import com.example.bookadmin.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Aspect
@Component
public class LogAspect {

    @Autowired
    private SysLogService sysLogService;
    @Autowired
    private UserService userService;

    @Pointcut("execution(* com.example.bookadmin.controller..*(..))") // 控制器包路径
    public void logPointCut() {
    }

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long start = System.currentTimeMillis();

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        SysLog logEntry = new SysLog();
        String clientIP = JakartaServletUtil.getClientIP(request, null);
        logEntry.setIp(clientIP);
        logEntry.setUri(request.getRequestURI());
        logEntry.setMethod(request.getMethod());
        logEntry.setCreateTime(new Date());

        MethodSignature signature = (MethodSignature) point.getSignature();
        if (StpUtil.isLogin()) {
            Long userId = StpUtil.getLoginIdAsLong();
            User user = userService.getById(userId);
            if (user != null) {
                logEntry.setUsername(user.getUsername()); // 示例，可自定义解析登录用户
            }
        }
        logEntry.setParams(JSON.toJSONString(point.getArgs()));
        try {
            R result = (R) point.proceed();
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("code", result.getCode());
            resultMap.put("msg", result.getMsg());
            logEntry.setResult(JSON.toJSONString(resultMap));
            logEntry.setStatus(1);
            return result;
        } catch (Exception e) {
            logEntry.setStatus(0);
            logEntry.setErrorMsg(e.getMessage());
            throw e;
        } finally {
            if (!request.getMethod().equals(HttpMethod.GET.name()) && !logEntry.getUri().contains("/api/logs")) {
                sysLogService.save(logEntry); // 持久化日志
            }
            long time = System.currentTimeMillis() - start;
            log.info("请求路径: {}, 耗时: {}ms", logEntry.getUri(), time);
        }
    }
}
