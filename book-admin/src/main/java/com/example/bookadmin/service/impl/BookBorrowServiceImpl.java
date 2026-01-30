package com.example.bookadmin.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.bookadmin.entity.Book;
import com.example.bookadmin.entity.BookBorrow;
import com.example.bookadmin.mapper.BookBorrowMapper;
import com.example.bookadmin.mapper.BookMapper;
import com.example.bookadmin.service.BookBorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BookBorrowServiceImpl extends ServiceImpl<BookBorrowMapper, BookBorrow>
        implements BookBorrowService {

}
