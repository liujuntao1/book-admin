package com.example.bookadmin.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "登录请求参数")
@Data
public class LoginDTO {
    @Schema(description = "用户名", example = "admin")
    private String username;

    @Schema(description = "密码", example = "123456")
    private String password;
}
