package com.example.bookadmin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("user")
public class User {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String username;

    private String realname;

    private String password;

    private String phone;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    private String avatar;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
