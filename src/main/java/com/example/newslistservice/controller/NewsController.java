package com.example.newslistservice.controller;

import com.example.newslistservice.domain.News;
import com.example.newslistservice.dto.DetailNewsDTO;
import com.example.newslistservice.dto.NewsDetailDTO;
import com.example.newslistservice.dto.NewsListDTO;
import com.example.newslistservice.service.NewsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/news")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    @PostMapping
    public ResponseEntity<News> post(@Valid @RequestBody News news) {
        News createdNews = newsService.createNews(news);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdNews);
    }

    @PutMapping
    public void put(@Valid @RequestBody News news) {
        newsService.updateNews(news);

    }


    @GetMapping
    public NewsListDTO getNews(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        try {
            int start = Math.max(0, (page - 1) * pageSize);
            List<News> allNewsList = newsService.SelectNewsList();
            int newsNum = allNewsList.size();
            int allPage = (int) Math.ceil((double) newsNum / pageSize);

            int end = Math.min(start + pageSize, newsNum);

            if (start >= newsNum) {
                return NewsListDTO.builder()
                        .newsList(List.of())
                        .newsNum(newsNum)
                        .allPage(allPage)
                        .pageSize(pageSize)
                        .page(page)
                        .build();
            }

            List<News> paginatedNewsList = allNewsList.subList(start, end);

            return NewsListDTO.builder()
                    .newsList(paginatedNewsList)
                    .newsNum(newsNum)
                    .allPage(allPage)
                    .pageSize(pageSize)
                    .page(page)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("뉴스 목록 처리 중 오류 발생", e);
        }
    }

    @GetMapping("/detail")
    public NewsDetailDTO getNewsId(@RequestParam Long id) {
        return newsService.getNewsId(id);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestBody List<Long> ids) {
        newsService.removeNews(ids);
    }



}


