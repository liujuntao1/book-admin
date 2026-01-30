package com.example.bookadmin.service;

import com.example.bookadmin.controller.dto.MenuDTO;

import java.util.List;

public interface RolePermissionService {
    void assignMenusToRole(Long roleId, List<Long> menuIds);
    List<Long> getMenuIdsByRoleId(Long roleId);

    void assignRolesToUser(Long userId, List<Long> roleIds);
    List<Long> getRoleIdsByUserId(Long userId);

}
