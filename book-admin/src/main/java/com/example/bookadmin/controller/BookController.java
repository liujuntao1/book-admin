package com.example.bookadmin.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.bookadmin.common.R;
import com.example.bookadmin.controller.dto.BookBorrowDTO;
import com.example.bookadmin.controller.dto.BookPageListDTO;
import com.example.bookadmin.controller.query.BookPageQuery;
import com.example.bookadmin.entity.*;
import com.example.bookadmin.service.BookBorrowService;
import com.example.bookadmin.service.BookService;
import com.example.bookadmin.service.UserFavoriteBookService;
import com.example.bookadmin.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookService bookService;
    @Autowired
    private BookBorrowService bookBorrowService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserFavoriteBookService favoriteService;

    /**
     * 查询图书列表（分页）
     */
    @PostMapping("pageList")
    public R<?> pageList(@RequestBody BookPageQuery bookPageQuery) {
        QueryWrapper<Book> bookQueryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(bookPageQuery.getTitle())) {
            bookQueryWrapper.like("title", bookPageQuery.getTitle());
        }
        if (StringUtils.isNotBlank(bookPageQuery.getAuthor())) {
            bookQueryWrapper.like("author", bookPageQuery.getAuthor());
        }
        if (StringUtils.isNotBlank(bookPageQuery.getPublisher())) {
            bookQueryWrapper.like("publisher", bookPageQuery.getPublisher());
        }
        if (StringUtils.isNotBlank(bookPageQuery.getIsbn())) {
            bookQueryWrapper.like("isbn", bookPageQuery.getIsbn());
        }
        if (Objects.nonNull(bookPageQuery.getCategoryId())) {
            bookQueryWrapper.like("category_id", bookPageQuery.getCategoryId());
        }
        if (Objects.nonNull(bookPageQuery.getStartTime()) && Objects.nonNull(bookPageQuery.getEndTime())) {
            bookQueryWrapper.between("publish_time", bookPageQuery.getStartTime(), bookPageQuery.getEndTime());
        }
        bookQueryWrapper.orderByDesc("create_time");
        Page<Book> pageData = bookService.page(new Page<>(bookPageQuery.getPage(), bookPageQuery.getSize()), bookQueryWrapper);
        if (bookPageQuery.getQuerySource() == null) {
            bookPageQuery.setQuerySource(2);
        }
        //图书库查询，需要查询是否已收藏
        if (bookPageQuery.getQuerySource() == 1) {
            long userId = StpUtil.getLoginIdAsLong();
            Page<BookPageListDTO> bookPageListDTOPage = new Page<>();
            BeanUtils.copyProperties(pageData, bookPageListDTOPage);
            List<BookPageListDTO> bookPageListDTOS = new ArrayList<>();
            for (Book record : pageData.getRecords()) {
                BookPageListDTO bookPageListDTO = new BookPageListDTO();
                BeanUtils.copyProperties(record, bookPageListDTO);
                //查询是否收藏
                LambdaQueryWrapper<UserFavoriteBook> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(UserFavoriteBook::getUserId, userId)
                        .eq(UserFavoriteBook::getBookId, record.getId());
                boolean exists = favoriteService.count(wrapper) > 0;
                bookPageListDTO.setIsFavorite(exists);
                bookPageListDTOS.add(bookPageListDTO);
            }
            bookPageListDTOPage.setRecords(bookPageListDTOS);
            return R.success(bookPageListDTOPage);
        } else {
            return R.success(pageData);
        }
    }

    /**
     * 根据 ID 查询单本图书
     */
    @GetMapping("/{id}")
    public R<?> get(@PathVariable Long id) {
        return R.success(bookService.getById(id));
    }

    /**
     * 新增图书
     */
    @PostMapping
    public R<?> add(@RequestBody Book book) {
        book.setCreateTime(LocalDateTime.now());
        return R.success(bookService.save(book));
    }

    /**
     * 修改图书
     */
    @PutMapping
    public R<?> update(@RequestBody Book book) {
        book.setUpdateTime(LocalDateTime.now());
        return R.success(bookService.updateById(book));
    }

    /**
     * 删除图书
     */
    @DeleteMapping("/{id}")
    public R<?> delete(@PathVariable Long id) {
        return R.success(bookService.removeById(id));
    }


    /**
     * 根据图书id查询借阅记录
     */
    @GetMapping("getBorrowListByBookId/{id}")
    public R<?> getBorrowListByBookId(@PathVariable Long id,
                                      @RequestParam(required = false, defaultValue = "1") Integer page,
                                      @RequestParam(required = false, defaultValue = "10") Integer size) {
        Page<BookBorrow> bookBorrowPage = bookBorrowService.page(Page.of(page, size), new QueryWrapper<BookBorrow>().eq("book_id", id).orderByDesc("borrow_date"));
        Page<BookBorrowDTO> bookBorrowDTOPage = new Page<>();
        BeanUtils.copyProperties(bookBorrowPage, bookBorrowDTOPage);
        List<BookBorrowDTO> bookBorrowDTOS = new ArrayList<>();
        for (BookBorrow record : bookBorrowPage.getRecords()) {
            BookBorrowDTO bookBorrowDTO = new BookBorrowDTO();
            BeanUtils.copyProperties(record, bookBorrowDTO);
            Book book = bookService.getById(bookBorrowDTO.getBookId());
            if (null != book) {
                bookBorrowDTO.setBookName(book.getTitle());
            }
            User user = userService.getById(record.getUserId());
            if (null != user) {
                bookBorrowDTO.setUserName(user.getUsername());
            }
            bookBorrowDTOS.add(bookBorrowDTO);
        }
        bookBorrowDTOPage.setRecords(bookBorrowDTOS);
        return R.success(bookBorrowDTOPage);
    }
}
