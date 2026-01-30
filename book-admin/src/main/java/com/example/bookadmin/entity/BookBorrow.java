package com.example.bookadmin.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("book_borrow")
public class BookBorrow {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long bookId;
    private Long userId;
    private Integer status; // 0申请中，1已借出，2已拒绝
    private LocalDateTime borrowDate;
    private LocalDateTime returnDate;
    private LocalDateTime approvalDate;
    private String rejectionReason;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
