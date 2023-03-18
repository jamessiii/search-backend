package com.james.api.service;

import com.james.api.feign.BlogFeignClient;
import com.james.api.feign.dto.request.GetSearchBlogRequestDto;
import com.james.api.feign.dto.response.GetSearchBlogResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final BlogFeignClient blogFeignClient;

    private static final String AUTHORIZATION_KEY = "KakaoAK 5fcb5522bff8f9a5a9e3b91ce370d7c7";

    public GetSearchBlogResponseDto getBlogList(String keyword) {

        return blogFeignClient.getBlogList(AUTHORIZATION_KEY, keyword, null, null, null);
    }
}
