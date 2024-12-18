package com.example.newslistservice.domain;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import java.time.Instant;
import java.time.LocalDateTime;

@Data
@Builder
public class News {
    @Id
    private Long id; // Primary Key

    @NotBlank(message = "Title must not be blank.")
    private String title; // 제목

    @NotBlank(message = "Content must not be blank.")
    private String content; // 내용


    @Column("author_id")
    private Integer authorId; // 작성자 ID


    @CreatedDate
    @Column("created_at")
    private LocalDateTime createdAt; // 작성일

    @Column("img") // 이미지 경로
    private String img; // 이미지

//    @Version
//    private int version; // 낙관적 락 버전 관리
}
