package com.example.newslistservice.domain;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class News {
    @Id
    private Long id;

    @NotBlank(message = "Title must not be blank.")
    private String title;

    @NotBlank(message = "Content must not be blank.")
    private String content;

    @Column("author_id")
    private Integer authorId;

    @CreatedDate
    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("img") // 여러 이미지 경로를 ';'로 연결
    private String img;
}

