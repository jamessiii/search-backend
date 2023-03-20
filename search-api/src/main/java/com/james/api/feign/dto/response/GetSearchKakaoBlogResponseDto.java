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
public class GetSearchKakaoBlogResponseDto {

    @JsonAlias(value = "meta")
    private Meta meta;

    @JsonAlias(value = "documents")
    private List<Document> documentList = new ArrayList<>();

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Meta {

        // 검색된 문서 수
        @JsonAlias(value = "total_count")
        private Integer totalCount;

        // total_count 중 노출 가능 문서 수\n
        @JsonAlias(value = "pageable_count")
        private Integer pageableCount;

        // 현재 페이지가 마지막 페이지인지 여부, 값이 false면 page를 증가시켜 다음 페이지를 요청할 수 있음
        @JsonAlias(value = "is_end")
        private Boolean isEnd;

    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Document {

        // 블로그 글 제목
        @JsonAlias(value = "title")
        private String title;

        // 블로그 글 요약
        @JsonAlias(value = "contents")
        private String contents;

        // 블로그 글 URL
        @JsonAlias(value = "url")
        private String url;

        // 블로그의 이름
        @JsonAlias(value = "blogname")
        private String blogName;

        // 검색 시스템에서 추출한 대표 미리보기 이미지 URL, 미리보기 크기 및 화질은 변경될 수 있음
        @JsonAlias(value = "thumbnail")
        private String thumbNail;

        // 블로그 글 작성시간, ISO 8601\n[YYYY]-[MM]-[DD]T[hh]:[mm]:[ss].000+[tz]
        @JsonAlias(value = "datetime")
        private LocalDateTime dateTime;

    }
}
