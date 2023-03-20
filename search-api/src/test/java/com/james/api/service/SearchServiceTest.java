package com.james.api.service;

import com.james.api.dto.GetPopularKeywordListResponseDto;
import com.james.api.enumeration.SortEnum;
import com.james.api.feign.SearchKakaoFeignClient;
import com.james.api.feign.dto.response.GetSearchKakaoBlogResponseDto;
import com.james.core.entity.Search;
import com.james.core.exception.NotFoundSearchException;
import com.james.core.repository.SearchRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SearchServiceTest {

    private static final String TEST_NORMAL_KEYWORD = "TEST_NORMAL_KEYWORD";
    private static final SortEnum TEST_SORT = SortEnum.ACCURACY;
    private static final Integer TEST_NORMAL_PAGE = 1;
    private static final Integer TEST_NORMAL_SIZE = 10;

    private static final String TEST_BLOG_NAME = "TEST_BLOG_NAME";
    private static final String TEST_CONTENTS = "TEST_CONTENTS";
    private static final String TEST_TITLE = "TEST_TITLE";
    private static final String TEST_URL = "TEST_URL";
    private static final String TEST_THUMB_NAIL = "TEST_THUMBNAIL";
    private static final LocalDateTime TEST_DATE_TIME = LocalDateTime.of(2023,3,20,15,30,30);
    private static final Boolean TEST_IS_END = false;
    private static final Integer TEST_PAGEABLE_COUNT = 1;
    private static final Integer TEST_TOTAL_COUNT = 10;

    private static final Long TEST_ID = 3L;
    private static final Long TEST_CALL_COUNT = 30L;

    @InjectMocks
    private SearchService searchService;

    @Mock
    private SearchKakaoFeignClient searchKakaoFeignClient;

    @Mock
    private SearchRepository searchRepository;

    @Nested
    @DisplayName("getBlogList 메소드는")
    class DescribeGetBlogList {

        @BeforeEach
        void setup() {

            List<GetSearchKakaoBlogResponseDto.Document> documentList = new ArrayList<>();
            GetSearchKakaoBlogResponseDto.Document document = new GetSearchKakaoBlogResponseDto.Document();
            document.setBlogName(TEST_BLOG_NAME);
            document.setContents(TEST_CONTENTS);
            document.setTitle(TEST_TITLE);
            document.setUrl(TEST_URL);
            document.setThumbNail(TEST_THUMB_NAIL);
            document.setDateTime(TEST_DATE_TIME);
            documentList.add(document);

            GetSearchKakaoBlogResponseDto.Meta meta = new GetSearchKakaoBlogResponseDto.Meta();
            meta.setIsEnd(TEST_IS_END);
            meta.setPageableCount(TEST_PAGEABLE_COUNT);
            meta.setTotalCount(TEST_TOTAL_COUNT);

            GetSearchKakaoBlogResponseDto responseFromKakao = new GetSearchKakaoBlogResponseDto();
            responseFromKakao.setDocumentList(documentList);
            responseFromKakao.setMeta(meta);

            when(searchKakaoFeignClient
                    .getBlogList(any(), eq(TEST_NORMAL_KEYWORD), eq(TEST_SORT.getCode()), eq(TEST_NORMAL_PAGE), eq(TEST_NORMAL_SIZE)))
                    .thenReturn(responseFromKakao);
        }

        @Nested
        @DisplayName("정상적인 파라미터로 호출하면")
        class ContextWithNormalParameters {

            @BeforeEach
            void setup() {
                when(searchRepository.findByKeyword(TEST_NORMAL_KEYWORD)).thenThrow(new NotFoundSearchException());
            }

            @Test
            @DisplayName("searchKakaoFeignClient.getBLogList에 파라미터를 전달한다.")
            void itGiveToGetBLogListFromRequest() {

                searchService.getBlogList(TEST_NORMAL_KEYWORD, TEST_SORT, TEST_NORMAL_PAGE, TEST_NORMAL_SIZE);

                ArgumentCaptor<String> keywordCaptor = ArgumentCaptor.forClass(String.class);
                ArgumentCaptor<String> sortCaptor = ArgumentCaptor.forClass(String.class);
                ArgumentCaptor<Integer> pageCaptor = ArgumentCaptor.forClass(Integer.class);
                ArgumentCaptor<Integer> sizeCaptor = ArgumentCaptor.forClass(Integer.class);
                verify(searchKakaoFeignClient, times(1)).getBlogList
                        (any(), keywordCaptor.capture(), sortCaptor.capture(), pageCaptor.capture(), sizeCaptor.capture());

                String capturedKeyword = keywordCaptor.getValue();
                String capturedSort = sortCaptor.getValue();
                Integer capturedPage = pageCaptor.getValue();
                Integer capturedSize = sizeCaptor.getValue();

                assertThat(capturedKeyword).as("전달받은 keyword 확인").isEqualTo(TEST_NORMAL_KEYWORD);
                assertThat(capturedSort).as("전달받은 sort 확인").isEqualTo(TEST_SORT.getCode());
                assertThat(capturedPage).as("전달받은 page 확인").isEqualTo(TEST_NORMAL_PAGE);
                assertThat(capturedSize).as("전달받은 size 확인").isEqualTo(TEST_NORMAL_SIZE);

            }
        }

        @Nested
        @DisplayName("새로운 키워드를 파라미터로 호출하면")
        class ContextWithNewKeyword {

            Search savedSearch = new Search();

            @BeforeEach
            void setup() {

                savedSearch.setId(TEST_ID);
                savedSearch.setKeyword(TEST_NORMAL_KEYWORD);
                savedSearch.setCallCount(TEST_CALL_COUNT);

                when(searchRepository.findByKeyword(TEST_NORMAL_KEYWORD)).thenThrow(new NotFoundSearchException());
                when(searchRepository.save(any())).thenReturn(savedSearch);
            }

            @Test
            @DisplayName("새로운 Search 객체를 저장한다.")
            void itSaveHistoryAndNewSearch() {

                searchService.getBlogList(TEST_NORMAL_KEYWORD, TEST_SORT, TEST_NORMAL_PAGE, TEST_NORMAL_SIZE);

                ArgumentCaptor<Search> searchCaptor = ArgumentCaptor.forClass(Search.class);

                verify(searchRepository, times(1)).save(searchCaptor.capture());
                Search capturedSearch = searchCaptor.getValue();

                assertThat(capturedSearch.getId()).as("전달받은 search 객체의 id 확인").isNull();
                assertThat(capturedSearch.getKeyword()).as("전달받은 search 객체의 keyword 확인").isEqualTo(TEST_NORMAL_KEYWORD);
                assertThat(capturedSearch.getCallCount()).as("전달받은 search 객체의 callCount 확인").isEqualTo(1);
            }
        }

        @Nested
        @DisplayName("이미 저장된 적 있는 키워드를 파라미터로 호출하면")
        class ContextWithOldKeyword {

            Search search = new Search();

            @BeforeEach
            void setup() {

                search.setId(TEST_ID);
                search.setKeyword(TEST_NORMAL_KEYWORD);
                search.setCallCount(TEST_CALL_COUNT);

                when(searchRepository.findByKeyword(TEST_NORMAL_KEYWORD)).thenReturn(Optional.of(search));
            }

            @Test
            @DisplayName("Search 객체를 업데이트 한다.")
            void itSaveHistoryAndUpdateSearch() {

                searchService.getBlogList(TEST_NORMAL_KEYWORD, TEST_SORT, TEST_NORMAL_PAGE, TEST_NORMAL_SIZE);

                ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
                verify(searchRepository, times(1)).increaseCallCount(idCaptor.capture());
                Long capturedId = idCaptor.getValue();

                assertThat(capturedId).as("업데이트 하는 search 객체의 id 확인").isEqualTo(TEST_ID);
            }
        }
    }


    @Nested
    @DisplayName("getPopularKeywordList 메소드는")
    class DescribeGetPopularKeywordList {

        @Nested
        @DisplayName("호출하면")
        class ContextWithOldKeyword {

            @BeforeEach
            void setup() {

                Search search = new Search(TEST_NORMAL_KEYWORD);
                List<Search> searchList = new ArrayList<>();
                searchList.add(search);

                when(searchRepository.findTop10ByOrderByCallCountDesc()).thenReturn(searchList);
            }

            @Test
            @DisplayName("List<GetPopularKeywordListResponseDto> 를 리턴한다.")
            void itSaveHistoryAndUpdateSearch() {

                List<GetPopularKeywordListResponseDto> searchList = searchService.getPopularKeywordList();

                verify(searchRepository, times(1)).findTop10ByOrderByCallCountDesc();

                assertThat(searchList.get(0).getKeyword()).isEqualTo(TEST_NORMAL_KEYWORD);
            }
        }
    }
}