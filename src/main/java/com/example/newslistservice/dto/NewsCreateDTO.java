package com.example.newslistservice.dto;

import com.example.newslistservice.domain.News;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class NewsCreateDTO {
    private String title;
    private String content;
    private String img;

    public News toNews(){
        return News.builder()
                .title(title)
                .content(content)
                .img(img)
                .build();
    }

}
