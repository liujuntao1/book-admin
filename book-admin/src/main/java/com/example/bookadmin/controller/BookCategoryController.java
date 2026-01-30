package com.example.bookadmin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.bookadmin.common.CommonResult;
import com.example.bookadmin.entity.BookCategory;
import com.example.bookadmin.service.BookCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/book-categories")
public class BookCategoryController {

    @Autowired
    private BookCategoryService categoryService;

    @GetMapping
    public CommonResult<?> list(@RequestParam(defaultValue = "1") int page,
                                @RequestParam(defaultValue = "10") int size) {
        return CommonResult.success(categoryService.page(new Page<>(page, size)));
    }

    @PostMapping
    public CommonResult add(@RequestBody BookCategory category) {
        boolean save = categoryService.save(category);
        return CommonResult.success(save);
    }

    @PutMapping
    public CommonResult update(@RequestBody BookCategory category) {
        return CommonResult.success(categoryService.updateById(category));
    }

    @DeleteMapping("/{id}")
    public CommonResult delete(@PathVariable Long id) {
        return CommonResult.success(categoryService.removeById(id));
    }
}
