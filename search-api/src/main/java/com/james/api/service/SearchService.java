package com.james.api.service;

import com.james.api.dto.GetSearchBlogResponseDto;
import com.james.api.enumeration.SortEnum;
import com.james.api.feign.SearchKakaoFeignClient;
import com.james.api.feign.dto.response.GetSearchKakaoBlogResponseDto;
import com.james.core.entity.History;
import com.james.core.entity.Search;
import com.james.core.exception.NoResponseFromServerException;
import com.james.core.exception.NotFoundSearchException;
import com.james.core.repository.HistoryRepository;
import com.james.core.repository.SearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
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

    private final SearchRepository searchRepository;
    private final HistoryRepository historyRepository;
    private final SearchKakaoFeignClient searchKakaoFeignClient;

    public Page<GetSearchBlogResponseDto> getBlogList(String keyword, SortEnum sort, Integer page, Integer size) {

        saveHistory(keyword);

        String authorization = "KakaoAK " + apiKey;
        GetSearchKakaoBlogResponseDto responseFromKakao = new GetSearchKakaoBlogResponseDto();
        List<GetSearchBlogResponseDto> documentList = new ArrayList<>();

        try {
            responseFromKakao = searchKakaoFeignClient.getBlogList(authorization, keyword, sort.getCode(), page, size);

        } catch (NoResponseFromServerException noResponseFromServerException) {
            // 네이버 요청
        }
        for (GetSearchKakaoBlogResponseDto.Document data : responseFromKakao.getDocumentList()) {
            GetSearchBlogResponseDto tmpDocument = new GetSearchBlogResponseDto();
            BeanUtils.copyProperties(data, tmpDocument);
            documentList.add(tmpDocument);
        }
        Integer totalCount = responseFromKakao.getMeta().getTotalCount();
        return new PageImpl<>(documentList, PageRequest.of(page, size), totalCount);
    }

    private void saveHistory(String keyword) {

        Search search;
        boolean isNewKeyword = false;

        try {
            search = searchRepository.findByKeyword(keyword).orElseThrow(NotFoundSearchException::new);
        } catch (NotFoundSearchException notFoundSearchException) {
            isNewKeyword = true;
            search = new Search(keyword);
        }

        if (isNewKeyword) {
            search = searchRepository.save(search);
        }

        History history = new History();
        history.setSearch(search);
        historyRepository.save(history);

        if (!isNewKeyword) {
            Long callCount = historyRepository.countBySearch(search);
            search.setCallCount(callCount);
            searchRepository.save(search);
        }
    }
}
