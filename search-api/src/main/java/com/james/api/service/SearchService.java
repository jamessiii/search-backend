package com.james.api.service;

import com.james.api.dto.GetPopularKeywordListResponseDto;
import com.james.api.dto.GetSearchBlogResponseDto;
import com.james.api.enumeration.SortEnum;
import com.james.api.feign.SearchKakaoFeignClient;
import com.james.api.feign.SearchNaverFeignClient;
import com.james.api.feign.dto.response.GetSearchKakaoBlogResponseDto;
import com.james.api.feign.dto.response.GetSearchNaverBlogResponseDto;
import com.james.core.entity.History;
import com.james.core.entity.Search;
import com.james.core.exception.NaverApiPageIsTooLargeException;
import com.james.core.exception.NoResponseFromServerException;
import com.james.core.exception.NotFoundSearchException;
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

    @Value("${kakao.api-key}")
    private String apiKey;

    @Value("${naver.client-id}")
    private String clientId;

    @Value("${naver.client-secret}")
    private String secret;

    private final SearchRepository searchRepository;
    private final HistoryRepository historyRepository;
    private final SearchKakaoFeignClient searchKakaoFeignClient;
    private final SearchNaverFeignClient searchNaverFeignClient;

    public Page<GetSearchBlogResponseDto> getBlogList(String keyword, SortEnum sort, Integer page, Integer size) {

        saveHistory(InsertOrUpdateSearch(keyword));

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
                throw new NaverApiPageIsTooLargeException();
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

    private Search InsertOrUpdateSearch(String keyword) {

        Search search = searchRepository.findByKeyword(keyword);

        if (search != null) {
            searchRepository.increaseCallCount(search.getId());
            return search;
        }

        try {
            search = searchRepository.save(new Search(keyword));
        } catch (DataIntegrityViolationException dataIntegrityViolationException) {
            search = searchRepository.findByKeyword(keyword);
            if (search == null)
                throw new NotFoundSearchException();
            searchRepository.increaseCallCount(search.getId());
        }
        return search;
    }

    private void saveHistory(Search search){
        History history = new History();
        history.setSearch(search);
        historyRepository.save(history);
    }

    public List<GetPopularKeywordListResponseDto> getPopularKeywordList() {

        List<GetPopularKeywordListResponseDto> result = new ArrayList<>();
        List<Search> searchList = searchRepository.findTop10ByOrderByCallCountDesc();

        int index = 1;
        for (Search search : searchList) {
            GetPopularKeywordListResponseDto tmpResponseDto = new GetPopularKeywordListResponseDto();
            BeanUtils.copyProperties(search, tmpResponseDto);
            tmpResponseDto.setIndex(index++);
            result.add(tmpResponseDto);
        }

        return result;
    }
}
