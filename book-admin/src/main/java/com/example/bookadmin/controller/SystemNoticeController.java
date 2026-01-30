package com.example.bookadmin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.bookadmin.common.R;
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
    public R<Page<SystemNotice>> list(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size) {
        return R.success(noticeService.page(Page.of(page, size), new QueryWrapper<SystemNotice>().orderByDesc("publish_time")));
    }

    @PostMapping
    public R create(@RequestBody SystemNotice notice) {
        notice.setCreateTime(LocalDateTime.now());
        return R.success(noticeService.save(notice));
    }

    @PutMapping
    public R update(@RequestBody SystemNotice notice) {
        notice.setUpdateTime(LocalDateTime.now());
        return R.success(noticeService.updateById(notice));
    }

    @DeleteMapping("/{id}")
    public R delete(@PathVariable Long id) {
        return R.success(noticeService.removeById(id));
    }

    @GetMapping("/{id}")
    public R getById(@PathVariable Long id) {
        return R.success(noticeService.getById(id));
    }
}
