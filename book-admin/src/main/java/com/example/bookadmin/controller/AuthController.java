package com.example.bookadmin.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.bookadmin.common.CommonResult;
import com.example.bookadmin.controller.dto.LoginDTO;
import com.example.bookadmin.entity.User;
import com.example.bookadmin.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "认证管理")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;


    /**
     * 用户登录
     */
    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public CommonResult<?> login(@RequestBody @Valid LoginDTO loginDTO) {
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(User::getUsername, loginDTO.getUsername());
        User user = userService.getOne(wrapper);

        if (user == null || !user.getPassword().equals(loginDTO.getPassword())) {
            return CommonResult.fail("用户名或密码错误");
        }

        StpUtil.login(user.getId());
        return CommonResult.success(StpUtil.getTokenValue());
    }

    /**
     * 退出登录
     */
    @Operation(summary = "退出登录")
    @PostMapping("/logout")
    public CommonResult<?> logout() {
        StpUtil.logout();
        return CommonResult.success("退出登录成功");
    }

    /**
     * 是否已登录
     */
    @Operation(summary = "是否已登录")
    @GetMapping("/isLogin")
    public CommonResult<Boolean> isLogin() {
        return CommonResult.success(StpUtil.isLogin());
    }


}
