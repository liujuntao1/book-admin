package com.example.bookadmin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("sys_log")
public class SysLog {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String username;

    private String ip;

    private String uri;

    private String method;

    private String params;

    private String result;

    private Integer status; // 1成功，0失败

    private String errorMsg;

    private Date createTime;
}
