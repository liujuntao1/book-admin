package com.example.bookadmin.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.bookadmin.common.R;
import com.example.bookadmin.controller.dto.BookBorrowDTO;
import com.example.bookadmin.controller.dto.BorrowAuditPageDTO;
import com.example.bookadmin.entity.Book;
import com.example.bookadmin.entity.BookBorrow;
import com.example.bookadmin.entity.User;
import com.example.bookadmin.service.BookBorrowService;
import com.example.bookadmin.service.BookService;
import com.example.bookadmin.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/book/borrow")
public class BookBorrowController {

    @Autowired
    private BookService bookService;

    @Autowired
    private BookBorrowService bookBorrowService;
    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public R<Page<BookBorrowDTO>> list(@RequestParam int page, @RequestParam int size) {
        Page<BookBorrow> paged = bookBorrowService.page(Page.of(page, size));
        Page<BookBorrowDTO> bookBorrowDTOPage = new Page<>();
        BeanUtils.copyProperties(paged, bookBorrowDTOPage);
        List<BookBorrowDTO> bookBorrowDTOS = new ArrayList<>();
        for (BookBorrow record : paged.getRecords()) {
            BookBorrowDTO bookBorrowDTO = new BookBorrowDTO();
            User user = userService.getById(record.getUserId());
            Book book = bookService.getById(record.getBookId());
            BeanUtils.copyProperties(record, bookBorrowDTO);
            if (null != user) {
                bookBorrowDTO.setUserName(user.getUsername());
            }
            if (null != book) {
                bookBorrowDTO.setBookName(book.getTitle());
            }
            bookBorrowDTOS.add(bookBorrowDTO);
        }
        bookBorrowDTOPage.setRecords(bookBorrowDTOS);
        return R.success(bookBorrowDTOPage);
    }

    // 读者借书申请
    @PostMapping("/apply")
    public R<String> applyForBookBorrow(@RequestBody BookBorrow request) {
        // 获取图书信息
        Book book = bookService.getById(request.getBookId());
        QueryWrapper<BookBorrow> eq = new QueryWrapper<BookBorrow>()
                .eq("book_id", request.getBookId())
                .eq("user_id", request.getUserId())
                .in("status", Arrays.asList(0, 1));
        long count = bookBorrowService.count(eq);
        if (count > 0) {
            return R.fail("不可重复申请");
        }
        if (book == null) {
            return R.fail("图书不可借阅");
        }

        // 判断库存
        if (book.getStock() <= 0) {
            return R.fail("库存不足，无法借阅");
        }

        // 创建借阅申请
        BookBorrow borrow = new BookBorrow();
        borrow.setBookId(request.getBookId());
        borrow.setUserId(request.getUserId());
        borrow.setStatus(0); // 状态：申请中
        borrow.setBorrowDate(LocalDateTime.now());

        bookBorrowService.save(borrow);

        return R.success("借阅申请已提交");
    }

    // 审核借阅申请
    @PostMapping("/approve/{borrowId}")
    public R<String> approveBorrowRequest(@PathVariable Long borrowId) {
        BookBorrow borrow = bookBorrowService.getById(borrowId);

        if (borrow == null || borrow.getStatus() != 0) {
            return R.fail("借阅申请无效");
        }

        // 更新借阅状态
        Book book = bookService.getById(borrow.getBookId());
        if (book.getStock() <= 0) {
            return R.fail("库存不足，无法借阅");
        }

        book.setStock(book.getStock() - 1); // 减少库存
        bookService.updateById(book);

        borrow.setStatus(1); // 设置借阅为已借出
        borrow.setApprovalDate(LocalDateTime.now());
        bookBorrowService.updateById(borrow);

        return R.success("借阅申请已批准");
    }

    // 拒绝借阅申请
    @PostMapping("/reject/{borrowId}")
    public R<String> rejectBorrowRequest(@PathVariable Long borrowId, @RequestParam String reason) {
        BookBorrow borrow = bookBorrowService.getById(borrowId);

        if (borrow == null || borrow.getStatus() != 0) {
            return R.fail("借阅申请无效");
        }

        borrow.setStatus(2); // 状态：已拒绝
        borrow.setRejectionReason(reason); // 拒绝理由
        bookBorrowService.updateById(borrow);

        return R.success("借阅申请已拒绝");
    }


    @GetMapping("/auditList")
    public R<Page<BorrowAuditPageDTO>> auditList(@RequestParam int page, @RequestParam int size, @RequestParam int status) {
        LambdaQueryWrapper<BookBorrow> queryWrapper = new LambdaQueryWrapper<>();
        if (status == 1) {
            queryWrapper.in(BookBorrow::getStatus, Arrays.asList(1, 3));
        } else {
            queryWrapper.eq(BookBorrow::getStatus, status);
        }
        Page<BookBorrow> paged = bookBorrowService.page(Page.of(page, size), queryWrapper);
        Page<BorrowAuditPageDTO> borrowAuditPageDTOPage = new Page<>();
        BeanUtils.copyProperties(paged, borrowAuditPageDTOPage);
        List<BorrowAuditPageDTO> borrowAuditPageDTOS = new ArrayList<>();
        for (BookBorrow record : paged.getRecords()) {
            BorrowAuditPageDTO borrowAuditPageDTO = new BorrowAuditPageDTO();
            User user = userService.getById(record.getUserId());
            Book book = bookService.getById(record.getBookId());
            BeanUtils.copyProperties(record, borrowAuditPageDTO);
            if (null != user) {
                borrowAuditPageDTO.setUserName(user.getUsername());
            }
            if (null != book) {
                borrowAuditPageDTO.setBookTitle(book.getTitle());
            }
            borrowAuditPageDTOS.add(borrowAuditPageDTO);
        }
        borrowAuditPageDTOPage.setRecords(borrowAuditPageDTOS);
        return R.success(borrowAuditPageDTOPage);
    }


    @GetMapping("/borrowList")
    public R<Page<BorrowAuditPageDTO>> borrowList(@RequestParam int page, @RequestParam int size) {
        Long userId = StpUtil.getLoginIdAsLong();
        LambdaQueryWrapper<BookBorrow> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BookBorrow::getUserId, userId);
        Page<BookBorrow> paged = bookBorrowService.page(Page.of(page, size), queryWrapper);
        Page<BorrowAuditPageDTO> borrowAuditPageDTOPage = new Page<>();
        BeanUtils.copyProperties(paged, borrowAuditPageDTOPage);
        List<BorrowAuditPageDTO> borrowAuditPageDTOS = new ArrayList<>();
        for (BookBorrow record : paged.getRecords()) {
            BorrowAuditPageDTO borrowAuditPageDTO = new BorrowAuditPageDTO();
            User user = userService.getById(record.getUserId());
            Book book = bookService.getById(record.getBookId());
            BeanUtils.copyProperties(record, borrowAuditPageDTO);
            if (null != user) {
                borrowAuditPageDTO.setUserName(user.getUsername());
            }
            if (null != book) {
                borrowAuditPageDTO.setBookTitle(book.getTitle());
            }
            borrowAuditPageDTOS.add(borrowAuditPageDTO);
        }
        borrowAuditPageDTOPage.setRecords(borrowAuditPageDTOS);
        return R.success(borrowAuditPageDTOPage);
    }

    @PostMapping("/return/{borrowId}")
    public R<String> returnBook(@PathVariable Long borrowId) {
        BookBorrow borrow = bookBorrowService.getById(borrowId);
        if (borrow == null || borrow.getStatus() != 1) {
            return R.fail("还书失败，借阅记录不存在或状态不正确");
        }

        // 更新图书库存
        Book book = bookService.getById(borrow.getBookId());
        book.setStock(book.getStock() + 1);
        bookService.updateById(book);

        // 更新借阅记录状态
        borrow.setStatus(3); // 已归还
        borrow.setReturnDate(LocalDateTime.now());
        bookBorrowService.updateById(borrow);

        return R.success("归还成功");
    }


    /**
     * 是否还在借阅
     *
     * @param bookId
     * @param userId
     * @return
     */
    @GetMapping("isBeingBorrowed")
    public R isBeingBorrowed(@RequestParam String bookId, @RequestParam String userId) {
        QueryWrapper<BookBorrow> eq = new QueryWrapper<BookBorrow>()
                .eq("book_id", bookId)
                .eq("user_id", userId)
                .in("status", Arrays.asList(0, 1));
        long count = bookBorrowService.count(eq);
        return R.success(count > 0);
    }
}
