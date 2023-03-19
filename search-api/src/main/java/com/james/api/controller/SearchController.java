package com.james.api.controller;

import com.james.api.dto.GetSearchBlogResponseDto;
import com.james.api.enumeration.SortEnum;
import com.james.api.feign.dto.response.GetSearchKakaoBlogResponseDto;
import com.james.api.service.SearchService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/v1/search", produces = MediaType.APPLICATION_JSON_VALUE)
public class SearchController {

    private final SearchService searchService;

    /**
     * 블로그 검색 컨트롤러
     *
     * @param keyword 검색어
     */
    @GetMapping(value = "/blog")
    public Page<GetSearchBlogResponseDto> getBlogList(
            @RequestParam @Parameter(description = "검색어") String keyword,
            @RequestParam @Parameter(description = "현재 페이지 번호") SortEnum sort,
            @RequestParam @Parameter(description = "현재 페이지 번호") int page,
            @RequestParam @Parameter(description = "한 페이지에 보일 목록 수") int size) {
        return searchService.getBlogList(keyword, sort, page, size);
    }
}
