package com.james.api.service;

import com.james.api.dto.GetPopularKeywordListResponseDto;
import com.james.api.dto.GetSearchBlogResponseDto;
import com.james.api.enumeration.SortEnum;
import com.james.api.feign.SearchKakaoFeignClient;
import com.james.api.feign.SearchNaverFeignClient;
import com.james.api.feign.dto.GetSearchKakaoBlogResponseDto;
import com.james.api.feign.dto.GetSearchNaverBlogResponseDto;
import com.james.core.entity.History;
import com.james.core.entity.Search;
import com.james.core.exception.NoResponseFromServerException;
import com.james.core.exception.NotFoundSearchException;
import com.james.core.exception.RequestPageIsTooLargeUsingNaverApiException;
import com.james.core.repository.HistoryRepository;
import com.james.core.repository.SearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {

    /** Kakao Api Serve 통신용 API-Key */
    @Value("${kakao.api-key}")
    private String apiKey;

    /** Naver Api Serve 통신용 Client-Id */
    @Value("${naver.client-id}")
    private String clientId;

    /** Kakao Api Serve 통신용 Secret */
    @Value("${naver.client-secret}")
    private String secret;

    private final SearchRepository searchRepository;
    private final HistoryRepository historyRepository;
    private final SearchKakaoFeignClient searchKakaoFeignClient;
    private final SearchNaverFeignClient searchNaverFeignClient;

    /**
     * 블로그 검색 서비스
     * @param keyword   검색어
     * @param sort      정렬방법
     * @param page      현재 페이지 번호
     * @param size      한 페이지에 보일 목록 수
     * @return          {@link Page<GetSearchBlogResponseDto>} 형태로 반환합니다.
     */
    public Page<GetSearchBlogResponseDto> getBlogList(String keyword, SortEnum sort, Integer page, Integer size) {

        Search updatedSearch = insertOrUpdateSearch(keyword);
        historyRepository.save(new History(updatedSearch));

        String authorization = "KakaoAK " + apiKey;
        List<GetSearchBlogResponseDto> documentList = new ArrayList<>();
        Integer totalCount;

        try {
            GetSearchKakaoBlogResponseDto responseFromKakao = searchKakaoFeignClient.getBlogList(authorization, keyword, sort.getCodeKakao(), page, size);
            for (GetSearchKakaoBlogResponseDto.Document data : responseFromKakao.getDocumentList()) {
                GetSearchBlogResponseDto tmpDocument = new GetSearchBlogResponseDto();
                BeanUtils.copyProperties(data, tmpDocument);
                documentList.add(tmpDocument);
            }
            totalCount = responseFromKakao.getMeta().getTotalCount();
        } catch (NoResponseFromServerException noResponseFromServerException) {

            if (page > 100) {
                throw new RequestPageIsTooLargeUsingNaverApiException();
            }

            GetSearchNaverBlogResponseDto responseFromNaver = searchNaverFeignClient.getBlogList(clientId, secret, keyword, sort.getCodeNaver(), page, size);
            for (GetSearchNaverBlogResponseDto.Document data : responseFromNaver.getDocumentList()) {
                GetSearchBlogResponseDto tmpDocument = new GetSearchBlogResponseDto();
                BeanUtils.copyProperties(data, tmpDocument);
                documentList.add(tmpDocument);
            }
            totalCount = responseFromNaver.getTotalCount();
        }
        return new PageImpl<>(documentList, PageRequest.of(page, size), totalCount);
    }

    /**
     * 새로운 키워드면 새로 저장,
     * 기존에 호출된 적 있는 키워드라면 callCount를 업데이트 합니다.
     * @param keyword   검색 키워드
     * @return          {@link Search} 형태로 반환합니다.
     */
    private Search insertOrUpdateSearch(String keyword) {

        Search search = searchRepository.findByKeyword(keyword);

        if (search != null) {
            searchRepository.increaseCallCount(search.getId());
            return search;
        }

        try {
            search = searchRepository.save(new Search(keyword));
        } catch (DataIntegrityViolationException dataIntegrityViolationException) {
            search = searchRepository.findByKeyword(keyword);
            if (search == null) {
                throw new NotFoundSearchException();
            }
            searchRepository.increaseCallCount(search.getId());
        }
        return search;
    }

    /**
     * 인기검색어 Top10을 조회하여 반환한다.
     * @return {@link List<GetPopularKeywordListResponseDto>} 형태로 반환
     */
    public List<GetPopularKeywordListResponseDto> getPopularKeywordList() {

        List<GetPopularKeywordListResponseDto> result = new ArrayList<>();
        List<Search> searchList = searchRepository.findTop10ByOrderByCallCountDesc();

        for (Search search : searchList) {
            GetPopularKeywordListResponseDto tmpResponseDto = new GetPopularKeywordListResponseDto();
            BeanUtils.copyProperties(search, tmpResponseDto);
            tmpResponseDto.setIndex(searchList.indexOf(search) + 1);
            result.add(tmpResponseDto);
        }
        return result;
    }
}
