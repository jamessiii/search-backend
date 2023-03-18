package com.james.api.controller;

import com.james.api.feign.dto.response.GetSearchBlogResponseDto;
import com.james.api.service.SearchService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
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
    public GetSearchBlogResponseDto getBlogList(
            @Parameter(name = "keyword", description = "테스트 파라미터") @RequestParam String keyword) {
        return searchService.getBlogList(keyword);
    }
}
