package com.example.newslistservice.controller;

import com.example.newslistservice.domain.News;
import com.example.newslistservice.dto.CreateNewsRequestDTO;
import com.example.newslistservice.dto.NewsDetailDTO;
import com.example.newslistservice.dto.NewsListDTO;
import com.example.newslistservice.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/news")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CreateNewsRequestDTO> post(
            @RequestPart("title") String title,  // 제목 받기
            @RequestPart("content") String content,  // 내용 받기
            @RequestPart(value = "img", required = false) List<MultipartFile> images  // 이미지 파일들 받기
    ) throws IOException {
        System.out.println("이미지는 :::" + images);
        CreateNewsRequestDTO news = new CreateNewsRequestDTO();
        news.setTitle(title);
        news.setContent(content);

        // 이미지가 있다면 처리
        CreateNewsRequestDTO createdNews = newsService.createNews(news, images);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdNews);
    }

    @PostMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CreateNewsRequestDTO> put(
            @RequestParam("id") Long id,
            @RequestPart("title") String title,  // 제목 받기
            @RequestPart("content") String content,  // 내용 받기
            @RequestPart(value = "img", required = false) List<MultipartFile> images)
            throws IOException {
        System.out.println("이미지::::"+images);
        CreateNewsRequestDTO news = new CreateNewsRequestDTO();
        news.setId(id);
        news.setTitle(title);
        news.setContent(content);

        CreateNewsRequestDTO createdNews = newsService.updateNews(news, images);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdNews);

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