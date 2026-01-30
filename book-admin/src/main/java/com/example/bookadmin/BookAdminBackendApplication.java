package com.example.bookadmin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.bookadmin.mapper")
public class BookAdminBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookAdminBackendApplication.class, args);
    }

}
