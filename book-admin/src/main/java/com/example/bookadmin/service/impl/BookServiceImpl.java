package com.example.bookadmin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.bookadmin.entity.Book;
import com.example.bookadmin.mapper.BookMapper;
import com.example.bookadmin.service.BookService;
import org.springframework.stereotype.Service;


@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements BookService {
}
