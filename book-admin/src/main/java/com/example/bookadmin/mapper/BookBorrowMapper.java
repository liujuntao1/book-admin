package com.example.bookadmin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.bookadmin.entity.BookBorrow;
import com.example.bookadmin.entity.BookCategory;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BookBorrowMapper extends BaseMapper<BookBorrow> {
}