package com.example.newslistservice.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class NewsUpdateDTO {
    private String title;
    private String content;
    private List<MultipartFile> img;
}
