package com.example.bookadmin.controller.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookBorrowDTO {
    private Long id;
    private String bookName;
    private Long bookId;
    private String userName;
    private Integer status;
    private LocalDateTime borrowDate;
    private LocalDateTime returnDate;
    private String rejectionReason;
}
