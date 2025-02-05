package com.example.newslistservice.service;

import com.example.newslistservice.S3.S3Service;
import com.example.newslistservice.client.FileClient;
import com.example.newslistservice.domain.News;
import com.example.newslistservice.dto.CreateNewsRequestDTO;
import com.example.newslistservice.dto.DetailNewsDTO;
import com.example.newslistservice.dto.NewsDetailDTO;
import com.example.newslistservice.mapper.NewsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
    public CreateNewsRequestDTO createNews(CreateNewsRequestDTO news, List<MultipartFile> images) throws IOException {
        if (images != null) {
            System.out.println("이미지는 ::" + images);
            StringBuilder s3Urls = new StringBuilder();
            for (MultipartFile file : images) {
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
    public CreateNewsRequestDTO updateNews(CreateNewsRequestDTO news, List<MultipartFile> images) throws IOException {
        if (images != null) {
            System.out.println("이미지는 ::" + images);
            StringBuilder s3Urls = new StringBuilder();
            for (MultipartFile file : images) {
                String s3Url = s3Service.uploadFile(file);
                if (s3Urls.length()>0){
                    s3Urls.append(";");
                }
                s3Urls.append(s3Url);

            }
            news.setImg(s3Urls.toString());
        }
        // Mapper 호출하여 DB에 저장
        int result = newsMapper.updateNews(news);
        if (result > 0) {
            return news;
        } else {
            throw new RuntimeException("Failed to insert news");
        }
    }





    public List<News> SelectNewsList() {
        return newsMapper.selectNewsList();
    }

    public NewsDetailDTO getNewsId(Long id) {
        // 1. 뉴스 데이터 조회
        DetailNewsDTO newsList = newsMapper.selectNewsById(id);

        // 2. 이미지 경로 추출
        List<String> imgPaths = null;
        if (newsList.getImg() != null) { // null이면 그대로 둠
            if (!newsList.getImg().isEmpty()) { // 빈 문자열일 때만 처리
                imgPaths = Arrays.asList(newsList.getImg().split(";"));
            } else {
                imgPaths = List.of(); // 빈 문자열이면 빈 리스트로 설정
            }
        }

        List<String> imgList = null;

        // 3. 이미지 리스트가 존재하고 첫 번째 값이 비어있지 않을 때만 API 호출
        if (imgPaths != null && !imgPaths.isEmpty() && !imgPaths.get(0).isEmpty()) {
            imgList = fileClient.getImg(imgPaths);
            System.out.println("이미지 리스트는 :: " + imgList);
        }

        // 4. DTO 반환 (img가 null이면 null 그대로 유지)
        return NewsDetailDTO.builder()
                .title(newsList.getTitle())
                .content(newsList.getContent())
                .img(imgList) // imgList도 null이면 그대로 전달
                .build();
    }



    public void removeNews(List<Long> ids) {
        if (ids != null && !ids.isEmpty()) {
            newsMapper.deleteByIds(ids);
        }
    }


}