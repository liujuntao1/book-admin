package com.example.bookadmin.controller;

import com.example.bookadmin.common.CommonResult;
import com.example.bookadmin.service.RolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/role-permissions")
public class RolePermissionController {

    @Autowired
    private RolePermissionService rolePermissionService;

    // 给角色分配菜单权限
    @PostMapping("/role/{roleId}/menus")
    public CommonResult<String> assignMenus(@PathVariable Long roleId, @RequestBody List<Long> menuIds) {
        rolePermissionService.assignMenusToRole(roleId, menuIds);
        return CommonResult.success("菜单授权成功");
    }

    // 查询角色拥有的菜单ID列表
    @GetMapping("/role/{roleId}/menus")
    public CommonResult<List<Long>> getMenus(@PathVariable Long roleId) {
        return CommonResult.success(rolePermissionService.getMenuIdsByRoleId(roleId));
    }

    // 给用户分配角色
    @PostMapping("/user/{userId}/roles")
    public CommonResult<String> assignRoles(@PathVariable Long userId, @RequestBody List<Long> roleIds) {
        rolePermissionService.assignRolesToUser(userId, roleIds);
        return CommonResult.success("角色分配成功");
    }

    // 查询用户拥有的角色ID列表
    @GetMapping("/user/{userId}/roles")
    public CommonResult<List<Long>> getRoles(@PathVariable Long userId) {
        return CommonResult.success(rolePermissionService.getRoleIdsByUserId(userId));
    }
}
