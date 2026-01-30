package com.example.bookadmin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.bookadmin.entity.BookRecommend;
import com.example.bookadmin.mapper.BookRecommendMapper;
import com.example.bookadmin.service.BookRecommendService;
import org.springframework.stereotype.Service;

@Service
public class BookRecommendServiceImpl extends ServiceImpl<BookRecommendMapper, BookRecommend> implements BookRecommendService {
}
