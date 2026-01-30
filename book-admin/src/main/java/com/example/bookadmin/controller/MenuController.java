package com.example.bookadmin.controller;

import com.example.bookadmin.common.CommonResult;
import com.example.bookadmin.controller.dto.MenuDTO;
import com.example.bookadmin.entity.Menu;
import com.example.bookadmin.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menus")
public class MenuController {

    @Autowired
    private MenuService menuService;

    // 获取菜单树
    @GetMapping("/tree")
    public CommonResult<List<MenuDTO>> getMenuTree() {
        List<MenuDTO> menuDTOs = menuService.getMenuTree();
        return CommonResult.success(menuDTOs); // 返回 MenuDTO 类型的树形结构
    }

    // 添加菜单
    @PostMapping
    public CommonResult<Menu> addMenu(@RequestBody Menu menu) {
        Menu createdMenu = menuService.addMenu(menu);
        return CommonResult.success(createdMenu);
    }

    // 更新菜单
    @PutMapping
    public CommonResult<Menu> updateMenu(@RequestBody Menu menu) {
        Menu updatedMenu = menuService.updateMenu(menu);
        return CommonResult.success(updatedMenu);
    }

    // 删除菜单
    @DeleteMapping("/{id}")
    public CommonResult<Void> deleteMenu(@PathVariable Long id) {
        boolean success = menuService.deleteMenu(id);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.fail("删除失败");
    }


}
