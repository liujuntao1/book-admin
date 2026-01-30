package com.example.bookadmin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.bookadmin.common.R;
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
    public R<?> list(@RequestParam(defaultValue = "1") int page,
                     @RequestParam(defaultValue = "10") int size) {
        return R.success(categoryService.page(new Page<>(page, size)));
    }

    @PostMapping
    public R add(@RequestBody BookCategory category) {
        boolean save = categoryService.save(category);
        return R.success(save);
    }

    @PutMapping
    public R update(@RequestBody BookCategory category) {
        return R.success(categoryService.updateById(category));
    }

    @DeleteMapping("/{id}")
    public R delete(@PathVariable Long id) {
        return R.success(categoryService.removeById(id));
    }
}
