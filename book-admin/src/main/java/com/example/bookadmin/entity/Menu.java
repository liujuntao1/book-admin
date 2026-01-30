package com.example.bookadmin.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_menu")  // 对应数据库的表名
public class Menu {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long parentId;

    private String name;

    private String title;

    private String path;

    private String component;

    private String icon;

    private Integer type; // 0目录 1菜单 2按钮

    private Integer visible; // 1显示 0隐藏

    private Integer sort;

    private String perms;

    private Integer keepAlive;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
