package com.example.bookadmin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.bookadmin.entity.UserFavoriteBook;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserFavoriteBookMapper extends BaseMapper<UserFavoriteBook> {
}
