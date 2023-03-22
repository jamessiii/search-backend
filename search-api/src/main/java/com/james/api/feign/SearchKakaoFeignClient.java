package com.james.api.feign;

import com.james.api.feign.configuration.SearchKakaoConfig;
import com.james.api.feign.dto.GetSearchKakaoBlogResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "SearchKakaoFeignClient", url = "https://dapi.kakao.com/", configuration = SearchKakaoConfig.class)
public interface SearchKakaoFeignClient {

    /**
     * 카카오 검색 API Server 로부터 블로그를 조회해오는 메소드
     * @param authorizationHeader       Kakao API Server 와의 통신을 위한 Api-Key Header
     * @param query                     검색어
     * @param sort                      정렬방식
     * @param page                      요청 페이지 번호
     * @param size                      한 페이지에 보여줄 목록 수
     * @return {@link GetSearchKakaoBlogResponseDto} 형태로 리턴합니다.
     */
    @GetMapping(value = "/v2/search/blog?query={query}&sort={sort}&size={size}&page={page}")
    GetSearchKakaoBlogResponseDto getBlogList(@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
                                              @PathVariable String query, @PathVariable String sort, @PathVariable Integer page, @PathVariable Integer size);
}
