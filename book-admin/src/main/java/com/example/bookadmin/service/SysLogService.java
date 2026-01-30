package com.example.bookadmin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.bookadmin.controller.query.SysLogQuery;
import com.example.bookadmin.entity.SysLog;

public interface SysLogService extends IService<SysLog> {
    IPage<SysLog> getLogPage( SysLogQuery query);
}
