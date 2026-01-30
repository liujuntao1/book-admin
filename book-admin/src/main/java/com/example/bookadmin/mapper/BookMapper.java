package com.example.bookadmin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.bookadmin.entity.Book;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BookMapper extends BaseMapper<Book> {
}
