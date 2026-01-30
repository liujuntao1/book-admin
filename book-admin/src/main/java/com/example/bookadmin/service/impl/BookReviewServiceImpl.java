package com.example.bookadmin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.bookadmin.entity.BookReview;
import com.example.bookadmin.mapper.BookReviewMapper;
import com.example.bookadmin.service.BookReviewService;
import org.springframework.stereotype.Service;

@Service
public class BookReviewServiceImpl extends ServiceImpl<BookReviewMapper, BookReview> implements BookReviewService {
}
