package com.example.newslistservice.dto;

import com.example.newslistservice.domain.News;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class NewsListDTO {
    private List<News> newsList;
    private int newsNum;
    private int allPage;
    private int pageSize;
    private int page;

}
