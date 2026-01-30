package com.example.bookadmin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.bookadmin.common.R;
import com.example.bookadmin.entity.Role;
import com.example.bookadmin.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    // 获取全部角色
    @GetMapping
    public R<Page<Role>> listRoles(@RequestParam(defaultValue = "1") Integer page,
                                   @RequestParam(defaultValue = "10") Integer size) {
        return R.success(roleService.page(Page.of(page, size)));
    }

    // 根据 ID 查询角色
    @GetMapping("/{id}")
    public R<Role> getById(@PathVariable Long id) {
        return R.success(roleService.getById(id));
    }

    // 新增角色
    @PostMapping
    public R<String> addRole(@RequestBody Role role) {
        roleService.save(role);
        return R.success("角色添加成功");
    }

    // 修改角色
    @PutMapping
    public R<String> updateRole(@RequestBody Role role) {
        roleService.updateById(role);
        return R.success("角色修改成功");
    }

    // 删除角色
    @DeleteMapping("/{id}")
    public R<String> deleteRole(@PathVariable Long id) {
        roleService.removeById(id);
        return R.success("角色删除成功");
    }
}
