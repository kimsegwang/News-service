package com.example.newslistservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class NewsDetailDTO {
    private String title;
    private String content;
    private List<String> img;
}
