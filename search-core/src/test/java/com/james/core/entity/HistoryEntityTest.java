package com.james.core.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HistoryEntityTest {

    public static final long TEST_ID = 1L;
    private static Search TEST_SEARCH;

    @BeforeEach
    void init() {
        TEST_SEARCH = new Search();
    }

    @Nested
    @DisplayName("History 엔티티는")
    class DescribeHistoryEntity {

        @Nested
        @DisplayName("정상적인 데이터로 생성자가 호출되면")
        class ContextWithConstructor {

            @Test
            @DisplayName("전달받은 데이터를 가진 History 객체가 생성된다.")
            void itReturnsHistory() {

                History history = new History(TEST_ID, TEST_SEARCH);

                assertEquals(TEST_ID, history.getId());
                assertEquals(TEST_SEARCH, history.getSearch());

            }
        }
    }
}
