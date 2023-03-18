package com.james.api.feign.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetSearchBlogRequestDto {

    private String query;

    private String sort;

    private Integer page;

    private Integer size;
}
