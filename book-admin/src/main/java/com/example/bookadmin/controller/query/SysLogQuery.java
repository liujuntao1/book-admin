package com.example.bookadmin.controller.query;

import lombok.Data;

@Data
public class SysLogQuery {
    private String username;
    private String uri;
    private String method;
    private Integer status;
    private String startTime; // 可改为 Date 类型，格式化处理
        private String endTime;
    private Integer page;
    private Integer size;
}
