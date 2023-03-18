package com.james.core.exception;

import com.james.core.error.ErrorCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BaseExceptionTest {

    @Nested
    @DisplayName("BaseException 은")
    class DescribeBaseException {

        @Nested
        @DisplayName("Custom Exception 이 발생하면")
        class ContextWithOccurCustomException {

            BaseException returnException() {
                return new NotFoundSearchException();
            }

            @Test
            @DisplayName("에러코드와 메시지를 리턴한다")
            void itReturns() {
                BaseException baseException = returnException();
                Assertions.assertThat(baseException.getErrorCode()).isEqualTo(ErrorCode.NOT_FOUND_SEARCH);
                Assertions.assertThat(baseException.getLogMessage()).isEqualTo(ErrorCode.NOT_FOUND_SEARCH.getMessage());
            }
        }
    }
}
