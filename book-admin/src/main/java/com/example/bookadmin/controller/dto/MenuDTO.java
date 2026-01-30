package com.example.bookadmin.controller.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class MenuDTO {
    private Long id;
    private String name;
    private String title;
    private String path;
    private String component;
    private String icon;

    private Integer type; // 0目录 1菜单 2按钮

    private LocalDateTime createTime;

    private MetaDTO meta;

    private List<MenuDTO> children;
}
