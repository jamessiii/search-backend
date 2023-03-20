package com.james.core.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SearchEntityTest {

    private static String TEST_KEYWORD = "TEST_KEYWORD";

    @Nested
    @DisplayName("Search 엔티티는")
    class DescribeSearchEntity {

        @Nested
        @DisplayName("정상적인 데이터로 생성자가 호출되면")
        class ContextWithConstructor {

            @Test
            @DisplayName("전달받은 데이터를 가진 Search 객체가 생성된다.")
            void itReturnsSearch() {

                Search search = new Search(TEST_KEYWORD);

                assertEquals(TEST_KEYWORD, search.getKeyword());
                assertEquals(1, search.getCallCount());

            }
        }
    }
}
