package com.example.newslistservice.service;

import com.example.newslistservice.domain.News;
import com.example.newslistservice.mapper.NewsMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NewsService {

    private final NewsMapper newsMapper;

    public NewsService(NewsMapper newsMapper) {
        this.newsMapper = newsMapper;
    }

    @Transactional
    public News createNews(News news) {
        int result = newsMapper.insertNews(news);// Mapper 호출
        if (result > 0) {
            return news;
        } else {
            throw new RuntimeException("Failed to insert news");
        }
    }

    public List<News> SelectNewsList() {
        return newsMapper.selectNewsList();
    }

    public News getNewsId(Long id) {
        return newsMapper.selectNewsById(id);
    }

    public void removeNews(List<Long> ids) {
        if (ids != null && !ids.isEmpty()) {
            newsMapper.deleteByIds(ids);
        }
    }

    @Transactional
    public News updateNews(Long id, News news) {
        int result = newsMapper.updateNews(id, news);// Mapper 호출
        if (result > 0) {
            return news;
        } else {
            throw new RuntimeException("Failed to update news");
        }
    }
}