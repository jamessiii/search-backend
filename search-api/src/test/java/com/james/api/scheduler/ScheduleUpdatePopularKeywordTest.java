package com.james.api.scheduler;

import com.james.core.entity.History;
import com.james.core.entity.Search;
import com.james.core.repository.HistoryRepository;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScheduleUpdatePopularKeywordTest {

    @InjectMocks
    private ScheduleUpdatePopularKeyword scheduleUpdatePopularKeyword;

    @Mock
    private HistoryRepository historyRepository;

    @Mock
    private SearchRepository searchRepository;

    @Nested
    @DisplayName("deleteExpiredHistoryAndUpdateSearchCallCount 메소드는")
    class DescribeDeleteExpiredHistoryAndUpdateSearchCallCount {

        private static final Long TEST_ID = 1L;
        private static final String TEST_NORMAL_KEYWORD = "TEST_NORMAL_KEYWORD";

        @Nested
        @DisplayName("기간만료된 history 가 있는 경우")
        class ContextWithExpiredHistoryList{

            @BeforeEach
            void setup() {

                Search search = new Search(TEST_NORMAL_KEYWORD);
                search.setId(TEST_ID);

                History history = new History(search);
                List<History> historyList = new ArrayList<>();
                historyList.add(history);

                when(historyRepository.findByCreatedAtBefore(any())).thenReturn(Optional.of(historyList));
                when(searchRepository.findById(anyLong())).thenReturn(search);
            }

            @Test
            @DisplayName("해당 History 를 삭제하고 Search 를 업데이트 한다.")
            void itDeleteExpiredHistoryAndUpdateSearch() {

                scheduleUpdatePopularKeyword.deleteExpiredHistoryAndUpdateSearchCallCount();

                @SuppressWarnings("unchecked")
                ArgumentCaptor<List<History>> historyListCaptor = ArgumentCaptor.forClass(ArrayList.class);
                verify(historyRepository, times(1)).deleteAll(historyListCaptor.capture());

                List<History> capturedHistoryList = historyListCaptor.getValue();
                assertThat(capturedHistoryList).hasSize(1);
                assertThat(capturedHistoryList.get(0)).isNotNull();
                assertThat(capturedHistoryList.get(0).getSearch()).isNotNull();
                assertThat(capturedHistoryList.get(0).getSearch().getId()).isEqualTo(TEST_ID);

                ArgumentCaptor<Long> searchIdCaptor = ArgumentCaptor.forClass(Long.class);
                ArgumentCaptor<Long> decreaseSizeCaptor = ArgumentCaptor.forClass(Long.class);
                verify(searchRepository, times(1)).decreaseCallCount(searchIdCaptor.capture(), decreaseSizeCaptor.capture());

                Long capturedSearchId = searchIdCaptor.getValue();
                Long capturedDecreaseSize = decreaseSizeCaptor.getValue();
                assertThat(capturedSearchId).isEqualTo(TEST_ID);
                assertThat(capturedDecreaseSize).isEqualTo(1);
            }
        }

        @Nested
        @DisplayName("기간만료된 history 가 있어서 callCount 감소 후 callCount가 0인 경우")
        class ContextWithExpiredHistoryListAndSearchCallCountIsZero{

            @BeforeEach
            void setup() {

                Search search = new Search(TEST_NORMAL_KEYWORD);
                search.setId(TEST_ID);

                History history = new History(search);
                List<History> historyList = new ArrayList<>();
                historyList.add(history);
                when(historyRepository.findByCreatedAtBefore(any())).thenReturn(Optional.of(historyList));

                search.setCallCount(0L);
                when(searchRepository.findById(anyLong())).thenReturn(search);
            }

            @Test
            @DisplayName("Search 가 삭제된다.")
            void itDeleteSearch() {

                scheduleUpdatePopularKeyword.deleteExpiredHistoryAndUpdateSearchCallCount();

                ArgumentCaptor<Search> searchCaptor = ArgumentCaptor.forClass(Search.class);
                verify(searchRepository, times(1)).delete(searchCaptor.capture());
            }
        }
    }
}