package com.example.bookadmin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.bookadmin.controller.dto.MenuDTO;
import com.example.bookadmin.entity.Menu;

import java.util.List;

public interface MenuService extends IService<Menu> {
    List<MenuDTO> getMenuTree();  // 获取菜单树

    Menu addMenu(Menu menu);  // 添加菜单

    Menu updateMenu(Menu menu);  // 更新菜单

    boolean deleteMenu(Long id);  // 删除菜单

    List<MenuDTO> getCurrentUserMenus();
}
