package com.james.api.controller;

import com.james.api.dto.GetPopularKeywordListResponseDto;
import com.james.api.dto.GetSearchBlogResponseDto;
import com.james.api.dto.ResponseDto;
import com.james.api.enumeration.SortEnum;
import com.james.api.service.SearchService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import java.util.List;

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
    public ResponseDto<Page<GetSearchBlogResponseDto>> getBlogList(
            @NotBlank(message = "검색어를 입력해주세요.")
            @RequestParam @Parameter(description = "검색어") String keyword,
            @RequestParam(required = false, defaultValue = "ACCURACY") @Parameter(description = "정렬") SortEnum sort,
            @RequestParam(required = false, defaultValue = "1") @Parameter(description = "현재 페이지 번호") Integer page,
            @RequestParam(required = false, defaultValue = "10") @Parameter(description = "한 페이지에 보일 목록 수") Integer size) {
        return ResponseDto.success(searchService.getBlogList(keyword, sort, page, size));
    }

    /**
     * 인기 검색어 조회 컨트롤러
     */
    @GetMapping(value = "/pop-keyword")
    public ResponseDto<List<GetPopularKeywordListResponseDto>> getPopularKeywordList() {
        return ResponseDto.success(searchService.getPopularKeywordList());
    }
}
