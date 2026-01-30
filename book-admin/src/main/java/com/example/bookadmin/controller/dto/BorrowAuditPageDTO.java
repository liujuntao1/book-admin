package com.example.bookadmin.controller.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author ljt
 * @date 2025/5/6
 * @description
 */
@Data
public class BorrowAuditPageDTO {
    private Long id;
    private Long bookId;
    private Long userId;
    private String bookTitle;
    private String userName;
    private Integer status;
    private LocalDateTime borrowDate;
    private LocalDateTime approvalDate;
    private LocalDateTime returnDate;
    private String rejectionReason;
}