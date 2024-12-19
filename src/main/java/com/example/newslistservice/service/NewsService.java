package com.example.newslistservice.service;

import com.example.newslistservice.client.FileClient;
import com.example.newslistservice.domain.News;
import com.example.newslistservice.dto.DetailNewsDTO;
import com.example.newslistservice.mapper.NewsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class NewsService {

    private final NewsMapper newsMapper;
    private final FileClient fileClient;

    // 이미지 경로를 세미콜론으로 구분하여 List<String>으로 변환하는 메서드
    public List<String> parseImagePaths(String img) {
        if (img != null && !img.isEmpty()) {
            return Arrays.asList(img.split(";"));
        }
        return Collections.emptyList();
    }

    // 뉴스 생성 메서드
    @Transactional
    public News createNews(News news) {
        // 다중 이미지 경로를 처리하여 List<String> 형태로 변환
        List<String> imagePaths = parseImagePaths(news.getImg());

        // 이미지를 세미콜론으로 구분된 하나의 문자열로 결합
        String imgString = String.join(";", imagePaths);
        news.setImg(imgString);  // 뉴스 객체에 결합된 이미지 경로 설정

        // Mapper 호출하여 DB에 저장
        int result = newsMapper.insertNews(news);
        if (result > 0) {
            return news;
        } else {
            throw new RuntimeException("Failed to insert news");
        }
    }





    public List<News> SelectNewsList() {
        return newsMapper.selectNewsList();
    }

    public DetailNewsDTO getNewsId(Long id) {
        // 1. 뉴스 데이터 조회
        List<DetailNewsDTO> newsList = newsMapper.selectNewsById(id);

        // 2. 이미지 경로 추출
        List<String> imgPaths = newsList.stream()
                .map(DetailNewsDTO::getImg)
                .collect(Collectors.toList());

        // 3. FeignClient를 사용해 이미지 정보 가져오기
        List<String> imgList = fileClient.getImg(imgPaths);
        String string = imgList.get(0);
        System.out.println("이미짚"+string);
        // Products와 인코딩된 이미지를 매칭하여 DTO 리스트를 생성
        List<DetailNewsDTO> detailedNews = IntStream.range(0, newsList.size())
                .mapToObj(i -> {
                    DetailNewsDTO newsDTO = newsList.get(i);
                    newsDTO.setImg(imgList.get(i));  // img 경로 업데이트
                    return newsDTO;
                })
                .collect(Collectors.toList());

        System.out.println("detailedNews: " + detailedNews);

        // 만약 여러 개의 뉴스 객체를 반환해야 한다면, List<DetailNewsDTO>로 반환
        return detailedNews.isEmpty() ? null : detailedNews.get(0); // 첫 번째 뉴스만 반환
    }


    public void removeNews(List<Long> ids) {
        if (ids != null && !ids.isEmpty()) {
            newsMapper.deleteByIds(ids);
        }
    }

    @Transactional
    public void updateNews(News news) {
        newsMapper.updateNews(news);// Mapper 호출
    }
}