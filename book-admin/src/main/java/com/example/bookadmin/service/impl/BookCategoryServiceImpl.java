package com.example.bookadmin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.bookadmin.entity.BookCategory;
import com.example.bookadmin.mapper.BookCategoryMapper;
import com.example.bookadmin.service.BookCategoryService;
import org.springframework.stereotype.Service;

@Service
public class BookCategoryServiceImpl extends ServiceImpl<BookCategoryMapper, BookCategory>
        implements BookCategoryService {
}