package com.example.bookadmin.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.bookadmin.common.CommonResult;
import com.example.bookadmin.controller.dto.MenuDTO;
import com.example.bookadmin.controller.dto.UpdatePwdDTO;
import com.example.bookadmin.entity.Role;
import com.example.bookadmin.entity.User;
import com.example.bookadmin.service.MenuService;
import com.example.bookadmin.service.RolePermissionService;
import com.example.bookadmin.service.RoleService;
import com.example.bookadmin.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "用户管理")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RolePermissionService rolePermissionService;

    @Autowired
    private MenuService menuService;
    @Autowired
    private RoleService roleService;

    /**
     * 获取用户列表（分页）
     */
    @Operation(summary = "获取用户列表")
    @GetMapping
    public CommonResult<?> list(@RequestParam(defaultValue = "1") Integer page,
                                @RequestParam(defaultValue = "10") Integer size) {
        Page<User> pageData = userService.page(Page.of(page, size));
        return CommonResult.success(pageData);
    }

    /**
     * 获取用户详情
     */
    @Operation(summary = "获取用户详情")
    @GetMapping("/{id}")
    public CommonResult<User> getById(@PathVariable Long id) {
        User user = userService.getById(id);
        return user != null ? CommonResult.success(user) : CommonResult.fail("用户不存在");
    }

    /**
     * 创建用户
     */
    @Operation(summary = "创建用户")
    @PostMapping
    public CommonResult<Boolean> create(@RequestBody @Valid User user) {
        user.setPassword("123456");
        return CommonResult.success(userService.save(user));
    }

    /**
     * 修改用户
     */
    @Operation(summary = "修改用户")
    @PutMapping
    public CommonResult<Boolean> update(@RequestBody @Valid User user) {
        if (user.getId() == null) return CommonResult.fail("ID不能为空");
        return CommonResult.success(userService.updateById(user));
    }

    /**
     * 删除用户
     */
    @Operation(summary = "删除用户")
    @DeleteMapping("/{id}")
    public CommonResult<Boolean> delete(@PathVariable Long id) {
        return CommonResult.success(userService.removeById(id));
    }

    /**
     * 修改密码
     */
    @Operation(summary = "修改密码")
    @PostMapping("/updatePwd")
    public CommonResult<Boolean> updatePwd(@RequestBody UpdatePwdDTO updatePwdDTO) {
        User user = userService.getById(updatePwdDTO.getUserId());
        if (null == user) {
            CommonResult.fail("未找到用户信息");
        }
        if (StringUtils.isBlank(updatePwdDTO.getOldPassword())) {
            CommonResult.fail("旧密码不能为空");
        }
        if (StringUtils.isBlank(updatePwdDTO.getNewPassword())) {
            CommonResult.fail("新密码不能为空");
        }

        if (updatePwdDTO.getNewPassword().equals(updatePwdDTO.getOldPassword())) {
            CommonResult.fail("新旧密码不能一致");
        }
        if (!user.getPassword().equals(updatePwdDTO.getOldPassword())) {
            CommonResult.fail("旧密码错误");
        }
        user.setPassword(updatePwdDTO.getNewPassword());
        user.setUpdateTime(LocalDateTime.now());
        return CommonResult.success(userService.updateById(user));
    }


    /**
     * 获取登录用户信息
     */
    @Operation(summary = "是否已登录")
    @GetMapping("/getLoginUserInfo")
    public CommonResult getLoginUserInfo() {
        long userId = StpUtil.getLoginIdAsLong();
        // 获取角色列表
        User user = userService.getById(userId);
        List<Long> roleIds = rolePermissionService.getRoleIdsByUserId(userId);// 比如：["admin"]
        List<Role> roles = roleService.listByIds(roleIds);
        // 获取权限标识
        List<MenuDTO> currentUserMenus = menuService.getCurrentUserMenus();// 比如：["user:add", "menu:view"]
        Map<String, Object> data = new HashMap<>();
        data.put("token", StpUtil.getTokenValue());
        data.put("user", user);
        data.put("roleIds", roleIds);
        data.put("roles", roles);
        data.put("menus", currentUserMenus);
        return CommonResult.success(data);
    }

}
