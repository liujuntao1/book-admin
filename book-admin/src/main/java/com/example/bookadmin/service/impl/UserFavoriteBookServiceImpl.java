package com.example.bookadmin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.bookadmin.entity.UserFavoriteBook;
import com.example.bookadmin.mapper.UserFavoriteBookMapper;
import com.example.bookadmin.service.UserFavoriteBookService;
import org.springframework.stereotype.Service;

@Service
public class UserFavoriteBookServiceImpl extends ServiceImpl<UserFavoriteBookMapper, UserFavoriteBook> implements UserFavoriteBookService {
}
