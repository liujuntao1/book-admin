package com.example.bookadmin.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.bookadmin.common.R;
import com.example.bookadmin.controller.dto.LoginDTO;
import com.example.bookadmin.controller.dto.MenuDTO;
import com.example.bookadmin.entity.Role;
import com.example.bookadmin.entity.User;
import com.example.bookadmin.service.MenuService;
import com.example.bookadmin.service.RolePermissionService;
import com.example.bookadmin.service.RoleService;
import com.example.bookadmin.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public R<?> login(@RequestBody @Valid LoginDTO loginDTO) {
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(User::getUsername, loginDTO.getUsername());
        User user = userService.getOne(wrapper);

        if (user == null || !user.getPassword().equals(loginDTO.getPassword())) {
            return R.fail("用户名或密码错误");
        }

        StpUtil.login(user.getId());
        return R.success(StpUtil.getTokenValue());
    }

    /**
     * 退出登录
     */
    @Operation(summary = "退出登录")
    @PostMapping("/logout")
    public R<?> logout() {
        StpUtil.logout();
        return R.success("退出登录成功");
    }

    /**
     * 是否已登录
     */
    @Operation(summary = "是否已登录")
    @GetMapping("/isLogin")
    public R<Boolean> isLogin() {
        return R.success(StpUtil.isLogin());
    }


}
