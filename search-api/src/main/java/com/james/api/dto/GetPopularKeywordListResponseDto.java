package com.james.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetPopularKeywordListResponseDto {

    // 순번
    private int index;

    // 검색어
    private String keyword;

    // 조회 수
    private Long callCount;

}
