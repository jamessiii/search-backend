package com.james.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class GetSearchBlogResponseDto {

    // 블로그 글 제목
    private String title;

    // 블로그 글 요약
    private String contents;

    // 블로그 글 URL
    private String url;

    // 블로그의 이름
    private String blogName;

    // 검색 시스템에서 추출한 대표 미리보기 이미지 URL, 미리보기 크기 및 화질은 변경될 수 있음
    private String thumbNail;

    // 블로그 글 작성시간, ISO 8601\n[YYYY]-[MM]-[DD]T[hh]:[mm]:[ss].000+[tz]
    private LocalDateTime dateTime;

}
