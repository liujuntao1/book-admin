package com.example.bookadmin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.bookadmin.common.CommonResult;
import com.example.bookadmin.entity.BookReview;
import com.example.bookadmin.service.BookReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
public class BookReviewController {

    @Autowired
    private BookReviewService bookReviewService;

    // 添加评价
    @PostMapping
    public CommonResult<?> addReview(@RequestBody BookReview review) {
        boolean success = bookReviewService.save(review);
        return success ? CommonResult.success("评价成功") : CommonResult.fail("评价失败");
    }

    // 修改评价
    @PutMapping
    public CommonResult<?> updateReview(@RequestBody BookReview review) {
        boolean success = bookReviewService.updateById(review);
        return success ? CommonResult.success("更新成功") : CommonResult.fail("更新失败");
    }

    // 删除评价
    @DeleteMapping("/{id}")
    public CommonResult<?> deleteReview(@PathVariable Long id) {
        boolean success = bookReviewService.removeById(id);
        return success ? CommonResult.success("删除成功") : CommonResult.fail("删除失败");
    }

    // 根据图书 ID 分页查询评价列表
    @GetMapping("/book/{bookId}")
    public CommonResult<?> listByBookId(
        @PathVariable Long bookId,
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        Page<BookReview> reviewPage = new Page<>(page, size);
        QueryWrapper<BookReview> wrapper = new QueryWrapper<>();
        wrapper.eq("book_id", bookId).orderByDesc("create_time");
        return CommonResult.success(bookReviewService.page(reviewPage, wrapper));
    }
}
