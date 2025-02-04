package com.example.newslistservice.service;

import com.example.newslistservice.S3.S3Service;
import com.example.newslistservice.client.FileClient;
import com.example.newslistservice.domain.News;
import com.example.newslistservice.dto.DetailNewsDTO;
import com.example.newslistservice.dto.NewsDetailDTO;
import com.example.newslistservice.mapper.NewsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
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
    private final S3Service s3Service;

    // 이미지 경로를 세미콜론으로 구분하여 List<String>으로 변환하는 메서드
    public List<String> parseImagePaths(String img) {
        if (img != null && !img.isEmpty()) {
            return Arrays.asList(img.split(";"));
        }
        return Collections.emptyList();
    }

    // 뉴스 생성 메서드
    @Transactional
    public News createNews(News news) throws IOException {
        if (news.getImg() != null && !news.getImg().isEmpty()) {
            List<String> str = Arrays.asList(news.getImg().split(";"));
            System.out.println("이미지는 ::" + str);
            StringBuilder s3Urls = new StringBuilder();
            for (String file : str) {
                String s3Url = s3Service.uploadFile(file);
                if (s3Urls.length()>0){
                    s3Urls.append(";");
                }
                s3Urls.append(s3Url);

            }
            news.setImg(s3Urls.toString());
        }
        // Mapper 호출하여 DB에 저장
        int result = newsMapper.insertNews(news);
        if (result > 0) {
            return news;
        } else {
            throw new RuntimeException("Failed to insert news");
        }
    }

    @Transactional
    public void updateNews(News news) throws IOException {
        if (news.getImg() != null && !news.getImg().isEmpty()) {
            List<String> str = Arrays.asList(news.getImg().split(";"));
            System.out.println("이미지는 ::" + str);
            StringBuilder s3Urls = new StringBuilder();
            for (String file : str) {
                String s3Url = s3Service.uploadFile(file);
                if (s3Urls.length()>0){
                    s3Urls.append(";");
                }
                s3Urls.append(s3Url);

            }
            news.setImg(s3Urls.toString());
        }
        newsMapper.updateNews(news);// Mapper 호출

    }





    public List<News> SelectNewsList() {
        return newsMapper.selectNewsList();
    }

    public NewsDetailDTO getNewsId(Long id) {
        // 1. 뉴스 데이터 조회
        DetailNewsDTO newsList = newsMapper.selectNewsById(id);

        // 2. 이미지 경로 추출
//        List<String> imgPaths = newsList.stream()
//                .map(DetailNewsDTO::getImg)
//                .collect(Collectors.toList());

        String[] arr = newsList.getImg().split(";");
        List<String> imgPaths = Arrays.asList(arr);
        List<String> imgList = List.of();
        if (!imgPaths.getFirst().isEmpty()) {
            imgList = fileClient.getImg(imgPaths);
            System.out.println("이미지 리스트는 :: "+imgList);
        }
        // 3. FeignClient를 사용해 이미지 정보 가져오기

        // Products와 인코딩된 이미지를 매칭하여 DTO 리스트를 생성

        return NewsDetailDTO.builder()
                .title(newsList.getTitle())
                .content(newsList.getContent())
                .img(imgList)
                .build();
    }


    public void removeNews(List<Long> ids) {
        if (ids != null && !ids.isEmpty()) {
            newsMapper.deleteByIds(ids);
        }
    }


}