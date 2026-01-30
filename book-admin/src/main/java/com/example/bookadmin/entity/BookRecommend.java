package com.example.bookadmin.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("book_recommend")
public class BookRecommend {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long bookId;

    private Integer type; // 1:热门 2:新书 3:分类

    private Integer sort;

    private String reason;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
