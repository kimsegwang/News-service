package com.example.newslistservice.mapper;

import com.example.newslistservice.domain.News;
import com.example.newslistservice.dto.DetailNewsDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface NewsMapper {
    int insertNews(News news);

    List<News> selectNewsList();

    DetailNewsDTO  selectNewsById(Long id);

    void deleteByIds(List<Long> ids);

    void updateNews(News news);

}
