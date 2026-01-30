package com.example.bookadmin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.bookadmin.common.R;
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
    public R<?> addReview(@RequestBody BookReview review) {
        boolean success = bookReviewService.save(review);
        return success ? R.success("评价成功") : R.fail("评价失败");
    }

    // 修改评价
    @PutMapping
    public R<?> updateReview(@RequestBody BookReview review) {
        boolean success = bookReviewService.updateById(review);
        return success ? R.success("更新成功") : R.fail("更新失败");
    }

    // 删除评价
    @DeleteMapping("/{id}")
    public R<?> deleteReview(@PathVariable Long id) {
        boolean success = bookReviewService.removeById(id);
        return success ? R.success("删除成功") : R.fail("删除失败");
    }

    // 根据图书 ID 分页查询评价列表
    @GetMapping("/book/{bookId}")
    public R<?> listByBookId(
        @PathVariable Long bookId,
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        Page<BookReview> reviewPage = new Page<>(page, size);
        QueryWrapper<BookReview> wrapper = new QueryWrapper<>();
        wrapper.eq("book_id", bookId).orderByDesc("create_time");
        return R.success(bookReviewService.page(reviewPage, wrapper));
    }
}
