package com.example.bookadmin.controller.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author ljt
 * @date 2025/5/8
 * @description
 */
@Data
public class BookRecommendDTO {
    private Long id;

    private Long bookId;

    private Integer type; // 1:热门 2:新书 3:分类

    private Integer sort;

    private String reason;

    private LocalDateTime createTime;

    private String bookTitle;
    private String bookAuthor;
    private String coverUrl;
}