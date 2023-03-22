package com.james.api.scheduler;

import com.james.core.entity.History;
import com.james.core.entity.Search;
import com.james.core.repository.HistoryRepository;
import com.james.core.repository.SearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduleUpdatePopularKeyword {

    private final HistoryRepository historyRepository;
    private final SearchRepository searchRepository;

    /**
     * 5분마다 실행되는 스케쥴러.
     * 실행시점 기준 15분 이내 데이터만 남기고 {@link History} 전부 삭제.
     * 삭제된 {@link History} 와의 릴레이션이 있는 {@link Search} 의 callCount 감소
     */
    @Scheduled(cron = "0 */5 * * * *")
    @SchedulerLock(name = "testShedLockJob", lockAtLeastFor = "PT4M59S", lockAtMostFor = "PT4M59S")
    public void deleteExpiredHistoryAndUpdateSearchCallCount() {
        
        LocalDateTime expiredTime = LocalDateTime.now().minusMinutes(15L);
        Optional<List<History>> expiredHistoryList = historyRepository.findByCreatedAtBefore(expiredTime);

        if (expiredHistoryList.isEmpty()) {
            return;
        }

        Map<Long, Long> historyCountToDeletePerSearchIdMap = new HashMap<>();
        for (History history : expiredHistoryList.get()) {
            Long searchId = history.getSearch().getId();
            Long count = historyCountToDeletePerSearchIdMap.containsKey(searchId) ? historyCountToDeletePerSearchIdMap.get(searchId) + 1L : 1L;
            historyCountToDeletePerSearchIdMap.put(searchId, count);
        }

        historyRepository.deleteAll(expiredHistoryList.get());

        for(Map.Entry<Long, Long> entry : historyCountToDeletePerSearchIdMap.entrySet()) {
            searchRepository.decreaseCallCount(entry.getKey(), entry.getValue());
        }
    }
}
