package com.example.bookadmin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_role")
public class Role {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;
    private String code;
    private String description;
    private Integer status;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
