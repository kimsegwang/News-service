package com.example.newslistservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@MapperScan("com.example.newslistservice.mapper")
@EnableFeignClients
public class NewsListServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NewsListServiceApplication.class, args);
    }

}
