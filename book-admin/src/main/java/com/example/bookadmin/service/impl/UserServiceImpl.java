package com.example.bookadmin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.bookadmin.entity.User;
import com.example.bookadmin.mapper.UserMapper;
import com.example.bookadmin.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
