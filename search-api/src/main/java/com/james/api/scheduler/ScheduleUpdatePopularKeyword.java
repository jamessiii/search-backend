package com.james.api.scheduler;

import com.james.core.entity.History;
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

@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduleUpdatePopularKeyword {

    private final HistoryRepository historyRepository;
    private final SearchRepository searchRepository;

    @Scheduled(cron = "0 */5 * * * *")
    @SchedulerLock(name = "testShedLockJob", lockAtLeastFor = "PT4M59S", lockAtMostFor = "PT4M59S")
    public void deleteExpiredHistoryAndUpdateSearchCallCount() {
        
        LocalDateTime expiredTime = LocalDateTime.now().minusMinutes(15L);
        List<History> expiredHistoryList = historyRepository.findByCreatedAtBefore(expiredTime);

        if (expiredHistoryList.isEmpty()) {
            return;
        }

        Map<Long, Long> countOfSearchToDecrease = new HashMap<>();
        for (History history : expiredHistoryList) {
            Long searchId = history.getSearch().getId();
            Long count = countOfSearchToDecrease.containsKey(searchId) ? countOfSearchToDecrease.get(searchId) + 1L : 1L;
            countOfSearchToDecrease.put(searchId, count);
        }

        historyRepository.deleteAll(expiredHistoryList);

        for(Map.Entry<Long, Long> entry : countOfSearchToDecrease.entrySet()) {
            searchRepository.decreaseCallCount(entry.getKey(), entry.getValue());
        }
    }
}
