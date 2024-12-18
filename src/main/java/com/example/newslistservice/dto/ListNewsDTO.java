package com.example.newslistservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ListNewsDTO {
    private Long id;
    private String title;
    private Integer authorId;
    private LocalDateTime createdAt;
}
