package com.example.bookadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.bookadmin.controller.query.SysLogQuery;
import com.example.bookadmin.entity.SysLog;
import com.example.bookadmin.mapper.SysLogMapper;
import com.example.bookadmin.service.SysLogService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements SysLogService {

    @Override
    public IPage<SysLog> getLogPage(SysLogQuery query) {
        QueryWrapper<SysLog> wrapper = new QueryWrapper<>();

        if (query != null) {
            if (StringUtils.hasText(query.getUsername())) {
                wrapper.like("username", query.getUsername());
            }
            if (StringUtils.hasText(query.getUri())) {
                wrapper.like("uri", query.getUri());
            }
            if (StringUtils.hasText(query.getMethod())) {
                wrapper.eq("method", query.getMethod());
            }
            if (query.getStatus() != null) {
                wrapper.eq("status", query.getStatus());
            }
            if (StringUtils.hasText(query.getStartTime()) && StringUtils.hasText(query.getEndTime())) {
                wrapper.between("create_time", query.getStartTime(), query.getEndTime());
            }
        }

        wrapper.orderByDesc("create_time");
        return this.page(new Page<>(query.getPage(), query.getSize()), wrapper);
    }
}
