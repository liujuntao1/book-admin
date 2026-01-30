package com.example.bookadmin.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class R<T> {
    private Integer code;
    private String msg;
    private T data;

    public static <T> R<T> success(T data) {
        return new R<>(200, "success", data);
    }

    public static <T> R<T> fail(String msg) {
        return new R<>(500, msg, null);
    }
}
