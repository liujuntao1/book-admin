package com.example.bookadmin.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author ljt
 * @date 2025/5/6
 * @description
 */
@Data
@TableName("book")
public class Book {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String title;          // 图书名称
    private String author;         // 作者
    private String publisher;      // 出版社
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate publishTime;      // 出版日期
    private String isbn;           // ISBN 编号
    private Integer stock;         // 库存数量
    private Long categoryId;       // 图书分类
    private String description;    // 简介
    private String coverUrl;    // 图书封面地址
    private BigDecimal price;    // 定价
    private String location;    // 位置

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}