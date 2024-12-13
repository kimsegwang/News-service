package com.example.newslistservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.newslistservice.mapper")
public class NewsListServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NewsListServiceApplication.class, args);
    }

}
