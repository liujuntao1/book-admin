package com.example.bookadmin.controller.query;

import lombok.Data;

import java.util.Date;

/**
 * @author ljt
 * @date 2025/5/8
 * @description
 */
@Data
public class BookPageQuery {
    private String title;
    private String author;
    private String isbn;
    private String publisher;
    private Date startTime;
    private Date endTime;
    private Long categoryId;
    private Integer page;
    private Integer size;
    private Integer querySource; //1、图书库、2、图书管理
}