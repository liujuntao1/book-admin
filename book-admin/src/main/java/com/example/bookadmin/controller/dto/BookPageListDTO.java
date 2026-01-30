package com.example.bookadmin.controller.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author ljt
 * @date 2025/5/9
 * @description
 */
@Data
public class BookPageListDTO {
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

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Boolean isFavorite; //是否收藏

}