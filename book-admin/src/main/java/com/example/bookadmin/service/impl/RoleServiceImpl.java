package com.example.bookadmin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.bookadmin.entity.Role;
import com.example.bookadmin.mapper.RoleMapper;
import com.example.bookadmin.service.RoleService;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
}
