package com.james.api.feign;

import com.james.api.feign.configuration.SearchKakaoConfig;
import com.james.api.feign.dto.response.GetSearchKakaoBlogResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "BlogFeignClient", url = "https://dapi.kakao.com/", configuration = SearchKakaoConfig.class)
public interface SearchKakaoFeignClient {

    @GetMapping(value = "/v2/search/blog?query={query}&sort={sort}&size={size}&page={page}")
    GetSearchKakaoBlogResponseDto getBlogList(@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
                                              @PathVariable String query, @PathVariable String sort, @PathVariable Integer page, @PathVariable Integer size);
}
