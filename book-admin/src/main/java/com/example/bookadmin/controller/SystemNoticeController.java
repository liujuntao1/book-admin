package com.example.bookadmin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.bookadmin.common.CommonResult;
import com.example.bookadmin.entity.SystemNotice;
import com.example.bookadmin.service.SystemNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/notices")
public class SystemNoticeController {

    @Autowired
    private SystemNoticeService noticeService;

    @GetMapping
    public CommonResult<Page<SystemNotice>> list(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size) {
        return CommonResult.success(noticeService.page(Page.of(page, size), new QueryWrapper<SystemNotice>().orderByDesc("publish_time")));
    }

    @PostMapping
    public CommonResult create(@RequestBody SystemNotice notice) {
        notice.setCreateTime(LocalDateTime.now());
        return CommonResult.success(noticeService.save(notice));
    }

    @PutMapping
    public CommonResult update(@RequestBody SystemNotice notice) {
        notice.setUpdateTime(LocalDateTime.now());
        return CommonResult.success(noticeService.updateById(notice));
    }

    @DeleteMapping("/{id}")
    public CommonResult delete(@PathVariable Long id) {
        return CommonResult.success(noticeService.removeById(id));
    }

    @GetMapping("/{id}")
    public CommonResult getById(@PathVariable Long id) {
        return CommonResult.success(noticeService.getById(id));
    }
}
