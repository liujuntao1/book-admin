package com.example.bookadmin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.bookadmin.common.R;
import com.example.bookadmin.controller.query.SysLogQuery;
import com.example.bookadmin.entity.SysLog;
import com.example.bookadmin.service.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/logs")
public class SysLogController {

    @Autowired
    private SysLogService sysLogService;

    @PostMapping("/page")
    public R<IPage<SysLog>> getLogPage(@RequestBody(required = false) SysLogQuery query) {
        return R.success(sysLogService.getLogPage(query));
    }
}
