package com.james.api.feign.decoder;

import com.james.core.exception.BadRequestException;
import com.james.core.exception.NoResponseFromServerException;
import feign.Request;
import feign.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
class FeignKakaoErrorDecoderTest {

    private static final String TEST_URL = "TEST_URL";
    private static final String TEST_METHOD_KEY = "TEST_METHOD_KEY";
    private static final String TEST_ERROR_MESSAGE = "TEST_ERROR_MESSAGE";

    @InjectMocks
    private FeignKakaoErrorDecoder feignKakaoErrorDecoder;

    @Nested
    @DisplayName("FeignKakaoErrorDecoder 의 decode 메소드는")
    class DescribeDecodeInFeignKakaoErrorDecoder {

        Response response;
        Map<String, Collection<String>> header = new LinkedHashMap<>();
        Request.Body body = Request.Body.create("");
        Request request = Request.create(Request.HttpMethod.GET, TEST_URL, header, body, null);

        @Nested
        @DisplayName("서버에서 500에러를 뱉으면")
        class ContextWith500Error {

            @BeforeEach
            void setup() {

                response = Response.builder()
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .request(request).build();
            }

            @Test
            @DisplayName("NoResponseFromServerException 을 던진다.")
            void itReturnNoResponseFromServerException() {

                Assertions.assertThatThrownBy(() -> feignKakaoErrorDecoder.decode(TEST_METHOD_KEY, response))
                        .isInstanceOf(NoResponseFromServerException.class);

            }
        }

        @Nested
        @DisplayName("서버에서 400에러를 뱉으면")
        class ContextWith400Error {

            @BeforeEach
            void setup() {
                response = Response.builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .body("{\"message\":\""+ TEST_ERROR_MESSAGE +"\"}", StandardCharsets.UTF_8)
                        .request(request).build();
            }

            @Test
            @DisplayName("BadRequestException 을 던진다.")
            void itReturnBadRequestException() {

                Assertions.assertThatThrownBy(() -> feignKakaoErrorDecoder.decode(TEST_METHOD_KEY, response))
                        .isInstanceOf(BadRequestException.class);
            }
        }
    }
}