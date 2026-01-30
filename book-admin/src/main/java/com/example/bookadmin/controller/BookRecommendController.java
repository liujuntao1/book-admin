package com.example.bookadmin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.bookadmin.common.R;
import com.example.bookadmin.controller.dto.BookRecommendDTO;
import com.example.bookadmin.entity.Book;
import com.example.bookadmin.entity.BookRecommend;
import com.example.bookadmin.service.BookRecommendService;
import com.example.bookadmin.service.BookService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/recommend")
public class BookRecommendController {

    @Autowired
    private BookRecommendService recommendService;
    @Autowired
    private BookService bookService;

    // 列表
    @GetMapping
    public R<Page<BookRecommendDTO>> list(@RequestParam(required = false, defaultValue = "1") Integer page,
                                          @RequestParam(required = false, defaultValue = "10") Integer size,
                                          @RequestParam(required = false) Integer type) {
        QueryWrapper<BookRecommend> wrapper = new QueryWrapper<>();
        if (type != null) {
            wrapper.eq("type", type);
        }
        wrapper.orderByDesc("sort");
        Page<BookRecommend> bookRecommendPage = recommendService.page(Page.of(page, size), wrapper);
        Page<BookRecommendDTO> bookRecommendDTOPage = new Page<>();
        BeanUtils.copyProperties(bookRecommendPage, bookRecommendDTOPage);
        List<BookRecommendDTO> bookRecommendDTOS = new ArrayList<>();
        for (BookRecommend record : bookRecommendPage.getRecords()) {
            BookRecommendDTO bookRecommendDTO = new BookRecommendDTO();
            BeanUtils.copyProperties(record, bookRecommendDTO);
            //查询图书信息
            Book book = bookService.getById(bookRecommendDTO.getBookId());
            if (null != book) {
                bookRecommendDTO.setBookAuthor(book.getAuthor());
                bookRecommendDTO.setBookTitle(book.getTitle());
                bookRecommendDTO.setCoverUrl(book.getCoverUrl());
            }
            bookRecommendDTOS.add(bookRecommendDTO);
        }
        bookRecommendDTOPage.setRecords(bookRecommendDTOS);
        return R.success(bookRecommendDTOPage);
    }

    // 新增
    @PostMapping
    public R add(@RequestBody BookRecommend recommend) {
        return R.success(recommendService.save(recommend));
    }

    // 修改
    @PutMapping
    public R update(@RequestBody BookRecommend recommend) {
        return R.success(recommendService.updateById(recommend));
    }

    // 删除
    @DeleteMapping("/{id}")
    public R delete(@PathVariable Long id) {
        return R.success(recommendService.removeById(id));
    }
}
