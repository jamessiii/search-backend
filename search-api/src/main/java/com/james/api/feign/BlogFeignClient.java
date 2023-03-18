package com.james.api.feign;

import com.james.api.feign.dto.request.GetSearchBlogRequestDto;
import com.james.api.feign.dto.response.GetSearchBlogResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "BlogFeignClient", url = "https://dapi.kakao.com/")
public interface BlogFeignClient {

    @GetMapping(value = "/v2/search/blog?query={query}&sort={sort}&size={size}&page={page}")
    GetSearchBlogResponseDto getBlogList(@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
                                         @PathVariable String query, @PathVariable String sort, @PathVariable Integer size, @PathVariable Integer page);
}
