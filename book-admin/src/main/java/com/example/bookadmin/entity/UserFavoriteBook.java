package com.example.bookadmin.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("user_favorite_books")
public class UserFavoriteBook {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Long bookId;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
