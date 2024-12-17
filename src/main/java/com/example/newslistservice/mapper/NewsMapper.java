package com.example.newslistservice.mapper;

import com.example.newslistservice.domain.News;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface NewsMapper {
    int insertNews(News news);

    List<News> selectNewsList();

    News selectNewsById(Long id);

    void deleteByIds(List<Long> ids);

    void updateNews(News news);

}
