package com.example.bookadmin.controller.dto;

import lombok.Data;

/**
 * @author ljt
 * @date 2025/5/9
 * @description
 */
@Data
public class UpdatePwdDTO {
    private Long userId;
    private String oldPassword;
    private String newPassword;
}