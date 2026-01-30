package com.example.bookadmin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.bookadmin.entity.Menu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MenuMapper extends BaseMapper<Menu> {
    List<Menu> selectByParentId(Long parentId);
}
