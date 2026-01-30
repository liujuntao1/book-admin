package com.example.bookadmin.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("system_notice")
public class SystemNotice {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String title;

    private String content;

    private String coverUrl;

    private Integer type;   // 1-系统公告，2-活动通知，3-借阅提醒

    private Integer status; // 1-启用，0-禁用

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishTime;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
