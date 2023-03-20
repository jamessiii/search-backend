package com.james.api.controller;

import com.james.api.dto.GetSearchBlogResponseDto;
import com.james.api.enumeration.SortEnum;
import com.james.api.service.SearchService;
import com.james.core.error.ErrorCode;
import com.james.core.exception.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = SearchController.class)
class SearchControllerTest {

    private static final String URL_GET_BLOG_LIST = "/v1/search/blog";
    private static final String TEST_NORMAL_KEYWORD = "TEST_NORMAL_KEYWORD";
    private static final SortEnum TEST_NORMAL_SORT = SortEnum.ACCURACY;
    private static final Integer TEST_NORMAL_PAGE = 1;
    private static final Integer TEST_ABNORMAL_PAGE = 999999999;
    private static final Integer TEST_NORMAL_SIZE = 10;

    private static final String TEST_BLOG_NAME = "TEST_BLOG_NAME";
    private static final String TEST_CONTENTS = "TEST_CONTENTS";
    private static final String TEST_TITLE = "TEST_TITLE";
    private static final String TEST_URL = "TEST_URL";
    private static final String TEST_THUMB_NAIL = "TEST_THUMBNAIL";
    private static final LocalDateTime TEST_DATE_TIME = LocalDateTime.of(2023,3,20,15,30,30);

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SearchService searchService;

    @Nested
    @DisplayName("GET /v1/search/blog API는")
    class DescribeGetBlogList {

        @Nested
        @DisplayName("정상적인 파라미터로 호출이 되면")
        class ContextWithNormalParameters {

            @BeforeEach
            void setup() {

                List<GetSearchBlogResponseDto> responseDtoList = new ArrayList<>();
                GetSearchBlogResponseDto responseDto = new GetSearchBlogResponseDto();
                responseDto.setBlogName(TEST_BLOG_NAME);
                responseDto.setContents(TEST_CONTENTS);
                responseDto.setTitle(TEST_TITLE);
                responseDto.setUrl(TEST_URL);
                responseDto.setThumbNail(TEST_THUMB_NAIL);
                responseDto.setDateTime(TEST_DATE_TIME);
                responseDtoList.add(responseDto);

                Page<GetSearchBlogResponseDto> result = new PageImpl<>(responseDtoList, PageRequest.of(TEST_NORMAL_PAGE, TEST_NORMAL_SIZE), responseDtoList.size());

                // Mock 객체 행동 지정
                when(searchService.getBlogList(TEST_NORMAL_KEYWORD, TEST_NORMAL_SORT, TEST_NORMAL_PAGE, TEST_NORMAL_SIZE)).thenReturn(result);

            }

            @Test
            @DisplayName("searchService.getBlog 를 호출하고 Page<GetSearchBlogResponseDto> 이 리턴된다.")
            void itReturnGetSearchBlogResponseDtoPage() throws Exception {

                mockMvc.perform(get(URL_GET_BLOG_LIST)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .param("keyword", TEST_NORMAL_KEYWORD)
                                .param("sort", String.valueOf(TEST_NORMAL_SORT))
                                .param("page", String.valueOf(TEST_NORMAL_PAGE))
                                .param("size", String.valueOf(TEST_NORMAL_SIZE)))
                        .andExpect(status().isOk())
                        .andDo(print())
                        .andExpect(jsonPath("$.content").exists())
                        .andExpect(jsonPath("$.content.[0].blogName", is(TEST_BLOG_NAME)))
                        .andExpect(jsonPath("$.content.[0].contents", is(TEST_CONTENTS)))
                        .andExpect(jsonPath("$.content.[0].title", is(TEST_TITLE)))
                        .andExpect(jsonPath("$.content.[0].url", is(TEST_URL)))
                        .andExpect(jsonPath("$.content.[0].thumbNail", is(TEST_THUMB_NAIL)))
                        .andExpect(jsonPath("$.content.[0].dateTime", is(TEST_DATE_TIME.toString())));

                ArgumentCaptor<String> keywordCaptor = ArgumentCaptor.forClass(String.class);
                ArgumentCaptor<SortEnum> sortCaptor = ArgumentCaptor.forClass(SortEnum.class);
                ArgumentCaptor<Integer> pageCaptor = ArgumentCaptor.forClass(Integer.class);
                ArgumentCaptor<Integer> sizeCaptor = ArgumentCaptor.forClass(Integer.class);
                verify(searchService, times(1)).getBlogList
                        (keywordCaptor.capture(), sortCaptor.capture(), pageCaptor.capture(), sizeCaptor.capture());

                String capturedKeyword = keywordCaptor.getValue();
                SortEnum capturedSort = sortCaptor.getValue();
                Integer capturedPage = pageCaptor.getValue();
                Integer capturedSize = sizeCaptor.getValue();

                assertThat(capturedKeyword).as("keyword 확인").isEqualTo(TEST_NORMAL_KEYWORD);
                assertThat(capturedSort).as("sort 확인").isEqualTo(TEST_NORMAL_SORT);
                assertThat(capturedPage).as("page 확인").isEqualTo(TEST_NORMAL_PAGE);
                assertThat(capturedSize).as("size 확인").isEqualTo(TEST_NORMAL_SIZE);

            }
        }

        @Nested
        @DisplayName("비정상적인 페이지 파라미터로 호출이 되면")
        class ContextWithAbNormalParameters {

            @BeforeEach
            void setup() {
                // Mock 객체 행동 지정
                doThrow(new BadRequestException()).when(searchService).getBlogList(TEST_NORMAL_KEYWORD, TEST_NORMAL_SORT, TEST_ABNORMAL_PAGE, TEST_NORMAL_SIZE);
            }

            @Test
            @DisplayName("searchService.getBlog 를 호출하고 BAD_REQUEST Exception 이 리턴된다.")
            void itReturnBadRequestException() throws Exception {

                mockMvc.perform(get(URL_GET_BLOG_LIST)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .param("keyword", TEST_NORMAL_KEYWORD)
                                .param("sort", String.valueOf(TEST_NORMAL_SORT))
                                .param("page", String.valueOf(TEST_ABNORMAL_PAGE))
                                .param("size", String.valueOf(TEST_NORMAL_SIZE)))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.error.code", is(ErrorCode.BAD_REQUEST.toString())));
            }
        }

        @Nested
        @DisplayName("파라미터 없이 호출이 되면")
        class ContextWithNoParameters {

            @Test
            @DisplayName("BAD_REQUEST Exception 이 리턴된다.")
            void itReturnBadRequestException() throws Exception {

                mockMvc.perform(get(URL_GET_BLOG_LIST)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
            }
        }
    }
}