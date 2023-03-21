package com.james.api.feign;

import com.james.api.feign.configuration.SearchNaverConfig;
import com.james.api.feign.dto.response.GetSearchNaverBlogResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "SearchNaverFeignClient", url = "https://openapi.naver.com/", configuration = SearchNaverConfig.class)
public interface SearchNaverFeignClient {

    @GetMapping(value = "/v1/search/blog.json?query={query}&sort={sort}&display={display}&start={start}")
    GetSearchNaverBlogResponseDto getBlogList(@RequestHeader(value = "X-Naver-Client-Id", required = true) String clientId,
                                              @RequestHeader(value = "X-Naver-Client-Secret", required = true) String secret,
                                              @PathVariable String query, @PathVariable String sort, @PathVariable Integer start, @PathVariable Integer display);
}
