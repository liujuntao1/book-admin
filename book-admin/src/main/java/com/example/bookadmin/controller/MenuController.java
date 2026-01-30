package com.example.bookadmin.controller;

import com.example.bookadmin.common.R;
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
    public R<List<MenuDTO>> getMenuTree() {
        List<MenuDTO> menuDTOs = menuService.getMenuTree();
        return R.success(menuDTOs); // 返回 MenuDTO 类型的树形结构
    }

    // 添加菜单
    @PostMapping
    public R<Menu> addMenu(@RequestBody Menu menu) {
        Menu createdMenu = menuService.addMenu(menu);
        return R.success(createdMenu);
    }

    // 更新菜单
    @PutMapping
    public R<Menu> updateMenu(@RequestBody Menu menu) {
        Menu updatedMenu = menuService.updateMenu(menu);
        return R.success(updatedMenu);
    }

    // 删除菜单
    @DeleteMapping("/{id}")
    public R<Void> deleteMenu(@PathVariable Long id) {
        boolean success = menuService.deleteMenu(id);
        if (success) {
            return R.success(null);
        }
        return R.fail("删除失败");
    }


}
