package com.james.api.feign;

import com.james.api.feign.configuration.SearchNaverConfig;
import com.james.api.feign.dto.GetSearchNaverBlogResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "SearchNaverFeignClient", url = "https://openapi.naver.com/", configuration = SearchNaverConfig.class)
public interface SearchNaverFeignClient {

    /**
     * 네이버 검색 API Server 로부터 블로그를 조회해오는 메소드
     * @param clientId      Naver API Server 와의 통신을 위한 Client-Id
     * @param secret        Naver API Server 와의 통신을 위한 Secret
     * @param query         검색어
     * @param sort          정렬방식
     * @param start         요청 페이지 번호
     * @param display       한 페이지에 보여줄 목록 수
     * @return {@link GetSearchNaverBlogResponseDto} 형태로 리턴합니다.
     */
    @GetMapping(value = "/v1/search/blog.json?query={query}&sort={sort}&display={display}&start={start}")
    GetSearchNaverBlogResponseDto getBlogList(@RequestHeader(value = "X-Naver-Client-Id", required = true) String clientId,
                                              @RequestHeader(value = "X-Naver-Client-Secret", required = true) String secret,
                                              @PathVariable String query, @PathVariable String sort, @PathVariable Integer start, @PathVariable Integer display);
}
