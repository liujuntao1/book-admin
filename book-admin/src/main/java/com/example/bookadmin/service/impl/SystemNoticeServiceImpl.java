package com.example.bookadmin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.bookadmin.entity.SystemNotice;
import com.example.bookadmin.mapper.SystemNoticeMapper;
import com.example.bookadmin.service.SystemNoticeService;
import org.springframework.stereotype.Service;

@Service
public class SystemNoticeServiceImpl extends ServiceImpl<SystemNoticeMapper, SystemNotice> implements SystemNoticeService {
}
