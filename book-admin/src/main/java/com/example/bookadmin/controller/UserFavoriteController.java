package com.example.bookadmin.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.bookadmin.common.R;
import com.example.bookadmin.entity.Book;
import com.example.bookadmin.entity.UserFavoriteBook;
import com.example.bookadmin.service.BookService;
import com.example.bookadmin.service.UserFavoriteBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static net.sf.jsqlparser.util.validation.metadata.MetadataContext.exists;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class UserFavoriteController {

    private final UserFavoriteBookService favoriteService;
    private final BookService bookService;

    // 添加收藏
    @PostMapping
    public R<String> add(@RequestBody UserFavoriteBook favorite) {
        // 可在这里判断是否已存在，避免前端重复添加
        LambdaQueryWrapper<UserFavoriteBook> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserFavoriteBook::getUserId, favorite.getUserId())
                .eq(UserFavoriteBook::getBookId, favorite.getBookId());
        UserFavoriteBook existing = favoriteService.getOne(wrapper);
        if (existing != null) {
            return R.success("已收藏，无需重复操作");
        }
        favorite.setCreateTime(LocalDateTime.now());
        favoriteService.save(favorite);
        return R.success("收藏成功");
    }

    // 取消收藏
    @DeleteMapping
    public R<String> remove(@RequestParam Long userId, @RequestParam Long bookId) {
        LambdaQueryWrapper<UserFavoriteBook> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserFavoriteBook::getUserId, userId)
                .eq(UserFavoriteBook::getBookId, bookId);
        boolean removed = favoriteService.remove(wrapper);
        return R.success(removed ? "取消成功" : "未找到收藏记录");
    }

    // 查询是否收藏（用于前端按钮状态）
    @GetMapping("/is-favorited")
    public R<Boolean> isFavorited(@RequestParam Long userId, @RequestParam Long bookId) {
        LambdaQueryWrapper<UserFavoriteBook> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserFavoriteBook::getUserId, userId)
                .eq(UserFavoriteBook::getBookId, bookId);
        boolean exists = favoriteService.count(wrapper) > 0;
        return R.success(exists);
    }


    /**
     * 获取收藏列表
     *
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/queryFavoritedList")
    public R<?> queryFavoritedList(@RequestParam(required = false, defaultValue = "1") Integer page,
                                   @RequestParam(required = false, defaultValue = "10") Integer size) {
        long userId = StpUtil.getLoginIdAsLong();
        LambdaQueryWrapper<UserFavoriteBook> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserFavoriteBook::getUserId, userId);
        List<UserFavoriteBook> list = favoriteService.list(wrapper);
        List<Long> bookIds = list.stream().map(UserFavoriteBook::getBookId).collect(Collectors.toList());
        LambdaQueryWrapper<Book> bookLambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (CollectionUtil.isNotEmpty(bookIds)) {
            bookLambdaQueryWrapper.in(Book::getId, bookIds);
        }
        Page<Book> bookPage = bookService.page(Page.of(page, size), bookLambdaQueryWrapper);
        return R.success(bookPage);
    }
}
