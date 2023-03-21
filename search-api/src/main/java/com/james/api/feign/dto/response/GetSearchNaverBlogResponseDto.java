package com.james.api.feign.dto.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetSearchNaverBlogResponseDto {

    @JsonAlias(value = "items")
    private List<Document> documentList = new ArrayList<>();

    // 검색된 문서 수
    @JsonAlias(value = "total")
    private Integer totalCount;

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Document {

        // 블로그 글 제목
        @JsonAlias(value = "title")
        private String title;

        // 블로그 글 요약
        @JsonAlias(value = "description")
        private String contents;

        // 블로그 글 URL
        @JsonAlias(value = "link")
        private String url;

        // 블로그의 이름
        @JsonAlias(value = "bloggername")
        private String blogName;

        // 블로그 글 작성시간
        @JsonAlias(value = "postdate")
        private LocalDateTime dateTime;

    }
}
