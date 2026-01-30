package com.example.bookadmin.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.bookadmin.controller.dto.MenuDTO;
import com.example.bookadmin.controller.dto.MetaDTO;
import com.example.bookadmin.entity.Menu;
import com.example.bookadmin.entity.RoleMenu;
import com.example.bookadmin.entity.UserRole;
import com.example.bookadmin.mapper.MenuMapper;
import com.example.bookadmin.mapper.RoleMenuMapper;
import com.example.bookadmin.mapper.UserRoleMapper;
import com.example.bookadmin.service.RolePermissionService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RolePermissionServiceImpl implements RolePermissionService {

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public void assignMenusToRole(Long roleId, List<Long> menuIds) {
        // 先删除旧的
        LambdaQueryWrapper<RoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoleMenu::getRoleId, roleId);
        roleMenuMapper.delete(wrapper);

        // 添加新的
        List<RoleMenu> list = menuIds.stream().map(menuId -> {
            RoleMenu rm = new RoleMenu();
            rm.setRoleId(roleId);
            rm.setMenuId(menuId);
            return rm;
        }).collect(Collectors.toList());
        list.forEach(roleMenuMapper::insert);
    }

    @Override
    public List<Long> getMenuIdsByRoleId(Long roleId) {
        return roleMenuMapper.selectList(new LambdaQueryWrapper<RoleMenu>()
                .eq(RoleMenu::getRoleId, roleId))
                .stream()
                .map(RoleMenu::getMenuId)
                .collect(Collectors.toList());
    }

    @Override
    public void assignRolesToUser(Long userId, List<Long> roleIds) {
        userRoleMapper.delete(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, userId));
        List<UserRole> list = roleIds.stream().map(roleId -> {
            UserRole ur = new UserRole();
            ur.setUserId(userId);
            ur.setRoleId(roleId);
            return ur;
        }).collect(Collectors.toList());
        list.forEach(userRoleMapper::insert);
    }

    @Override
    public List<Long> getRoleIdsByUserId(Long userId) {
        return userRoleMapper.selectList(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, userId))
                .stream()
                .map(UserRole::getRoleId)
                .collect(Collectors.toList());
    }

}
