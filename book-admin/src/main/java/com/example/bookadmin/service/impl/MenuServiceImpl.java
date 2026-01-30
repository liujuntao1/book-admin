package com.example.bookadmin.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.bookadmin.controller.dto.MenuDTO;
import com.example.bookadmin.controller.dto.MetaDTO;
import com.example.bookadmin.entity.Menu;
import com.example.bookadmin.entity.RoleMenu;
import com.example.bookadmin.entity.UserRole;
import com.example.bookadmin.mapper.MenuMapper;
import com.example.bookadmin.mapper.RoleMenuMapper;
import com.example.bookadmin.mapper.UserRoleMapper;
import com.example.bookadmin.service.MenuService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {
    @Autowired
    private RoleMenuMapper roleMenuMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;

    // 获取菜单树形结构
    @Override
    public List<MenuDTO> getMenuTree() {
        List<Menu> menus = baseMapper.selectList(null); // 查询所有菜单
        return buildMenuTree(menus, 0L); // 根菜单的 parentId 是 0
    }


    @Override
    public Menu addMenu(Menu menu) {
        menu.setCreateTime(LocalDateTime.now());
        menu.setUpdateTime(LocalDateTime.now());
        this.save(menu);
        return menu;
    }

    @Override
    public Menu updateMenu(Menu menu) {
        menu.setUpdateTime(LocalDateTime.now());
        this.updateById(menu);
        return menu;
    }

    @Override
    public boolean deleteMenu(Long id) {
        // 在删除之前可以做一下子菜单的判断，避免删除父菜单时有未删除的子菜单
        return this.removeById(id);
    }

    // 使用 BeanUtils 进行属性复制
    private List<MenuDTO> buildMenuTree(List<Menu> menus, Long parentId) {
        List<MenuDTO> menuTree = new ArrayList<>();
        for (Menu menu : menus) {
            if (menu.getParentId().equals(parentId)) {
                // 创建 MenuDTO 对象
                MenuDTO menuDTO = new MenuDTO();
                try {
                    // 使用 BeanUtils.copyProperties 复制属性
                    BeanUtils.copyProperties(menu, menuDTO);
                } catch (Exception e) {
                    e.printStackTrace(); // 处理复制过程中的异常
                }
                MetaDTO metaDTO = new MetaDTO();
                metaDTO.setIcon(menu.getIcon());
                metaDTO.setKeepAlive(menu.getKeepAlive());
                metaDTO.setTitle(menu.getName());
                menuDTO.setMeta(metaDTO);
                // 设置子菜单
                menuDTO.setChildren(buildMenuTree(menus, menu.getId())); // 递归设置子菜单
                menuTree.add(menuDTO);
            }
        }
        return menuTree;
    }

    @Override
    public List<MenuDTO> getCurrentUserMenus() {
        Long userId = StpUtil.getLoginIdAsLong();

        // 查询用户角色ID
        List<Long> roleIds = userRoleMapper.selectList(Wrappers.<UserRole>lambdaQuery()
                        .eq(UserRole::getUserId, userId))
                .stream().map(UserRole::getRoleId).toList();

        if (roleIds.isEmpty()) return Collections.emptyList();

        // 查询菜单ID
        List<Long> menuIds = roleMenuMapper.selectList(Wrappers.<RoleMenu>lambdaQuery()
                        .in(RoleMenu::getRoleId, roleIds))
                .stream().map(RoleMenu::getMenuId).distinct().toList();

        if (menuIds.isEmpty()) return Collections.emptyList();

        // 查询菜单
        List<Menu> menus = this.list(Wrappers.<Menu>lambdaQuery()
                .in(Menu::getId, menuIds)
                .eq(Menu::getVisible, 1)
                .orderByAsc(Menu::getSort));

        //因角色关联的菜单可能不包含菜单的上级（目录下有三个菜单，角色只关联了两个或一个菜单），导致前端无法展示完整菜单，
        Map<Long, Menu> menuMaps = menus.stream().collect(Collectors.toMap(Menu::getId, menu -> menu));
        Set<Long> missingParentIds = menus.stream().filter(m -> m.getParentId() != 0 && !menuMaps.containsKey(m.getParentId()))
                .map(Menu::getParentId)
                .collect(Collectors.toSet());
        if (!missingParentIds.isEmpty()) {
            List<Menu> parents = this.list(Wrappers.<Menu>lambdaQuery()
                    .in(Menu::getId, missingParentIds)
                    .eq(Menu::getVisible, 1));
            menus.addAll(parents);
        }
        return buildMenuTree(menus);
    }

    private List<MenuDTO> buildMenuTree(List<Menu> menus) {
        Map<Long, MenuDTO> dtoMap = new HashMap<>();
        List<MenuDTO> root = new ArrayList<>();

        for (Menu menu : menus) {
            MenuDTO dto = new MenuDTO();
            BeanUtils.copyProperties(menu, dto);

            MetaDTO meta = new MetaDTO();
            meta.setTitle(menu.getName());
            meta.setIcon(menu.getIcon());
            meta.setKeepAlive(menu.getKeepAlive());
            dto.setMeta(meta);

            dto.setChildren(new ArrayList<>());
            dtoMap.put(menu.getId(), dto);
        }

        for (Menu menu : menus) {
            MenuDTO dto = dtoMap.get(menu.getId());
            if (menu.getParentId() == 0) {
                root.add(dto);
            } else {
                MenuDTO parent = dtoMap.get(menu.getParentId());
                if (parent != null) {
                    parent.getChildren().add(dto);
                }
            }
        }

        return root;
    }
}
