package com.james.api.feign.decoder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.james.api.feign.dto.response.GetSearchKakaoBlogResponseDto;
import feign.Request;
import feign.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class FeignKakaoDecoderTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String TEST_URL = "TEST_URL";
    private static final String TEST_METHOD_KEY = "TEST_METHOD_KEY";
    private static final String TEST_ERROR_MESSAGE = "TEST_ERROR_MESSAGE";
    private static final String TEST_TITLE = "TEST_TITLE";
    private static final String TEST_CONTENTS = "TEST_CONTENTS";
    private static final String TEST_BLOGNAME = "TEST_BLOGNAME";
    private static final String TEST_THUMBNAIL = "TEST_THUMBNAIL";
    private static final LocalDateTime TEST_DATETIME = LocalDateTime.of(2023,3,20,15,30,30);
    private static final String TEST_DATETIME_ISO = "2023-03-20T06:30:30.000Z";
    private static final Integer TEST_TOTAL_COUNT = 10;
    private static final Integer TEST_PAGEABLE_COUNT = 1;
    private static final Boolean TEST_IS_END = false;



    @InjectMocks
    private FeignKakaoDecoder feignKakaoDecoder;

    @Nested
    @DisplayName("FeignKakaoDecoder decode 메소드는")
    class DescribeDecodeInFeignKakaoDecoder {

        Response response;
        Map<String, Collection<String>> header = new LinkedHashMap<>();
        Request.Body body = Request.Body.create("");
        Request request = Request.create(Request.HttpMethod.GET, TEST_URL, header, body, null);

        @Nested
        @DisplayName("서버에서 정상응답을 받으면")
        class ContextWithNormalResponse {

            @BeforeEach
            void setup() throws JsonProcessingException {

                Map<String, String> document = new LinkedHashMap<>();
                document.put("title", TEST_TITLE);
                document.put("contents", TEST_CONTENTS);
                document.put("url", TEST_URL);
                document.put("blogname", TEST_BLOGNAME);
                document.put("thumbnail", TEST_THUMBNAIL);
                document.put("datetime", TEST_DATETIME_ISO);

                List<Map<String, String>> documentList = new ArrayList<>();
                documentList.add(document);

                Map<String, String> meta = new LinkedHashMap<>();
                meta.put("total_count", TEST_TOTAL_COUNT.toString());
                meta.put("pageable_count", TEST_PAGEABLE_COUNT.toString());
                meta.put("is_end", TEST_IS_END.toString());

                Map<String, Object> responseBody = new LinkedHashMap<>();
                responseBody.put("meta", meta);
                responseBody.put("documents", documentList);

                String body = objectMapper.writeValueAsString(responseBody);
                response = Response.builder()
                        .status(HttpStatus.OK.value())
                        .body(body, StandardCharsets.UTF_8)
                        .request(request).build();
            }

            @Test
            @DisplayName("ResponseDto Type 의 Object 를 던진다.")
            void itReturnNoResponseFromServerException() throws IOException {

                Object responseDto = feignKakaoDecoder.decode(response, GetSearchKakaoBlogResponseDto.class);

                assertThat(responseDto.getClass()).isEqualTo(GetSearchKakaoBlogResponseDto.class);
                GetSearchKakaoBlogResponseDto response = (GetSearchKakaoBlogResponseDto)responseDto;
                assertThat(response.getMeta().getTotalCount()).isEqualTo(TEST_TOTAL_COUNT);
                assertThat(response.getMeta().getPageableCount()).isEqualTo(TEST_PAGEABLE_COUNT);
                assertThat(response.getMeta().getIsEnd()).isEqualTo(TEST_IS_END);
                assertThat(response.getDocumentList().get(0).getContents()).isEqualTo(TEST_CONTENTS);
                assertThat(response.getDocumentList().get(0).getBlogName()).isEqualTo(TEST_BLOGNAME);
                assertThat(response.getDocumentList().get(0).getDateTime()).isEqualTo(TEST_DATETIME);
                assertThat(response.getDocumentList().get(0).getTitle()).isEqualTo(TEST_TITLE);
                assertThat(response.getDocumentList().get(0).getThumbNail()).isEqualTo(TEST_THUMBNAIL);
                assertThat(response.getDocumentList().get(0).getUrl()).isEqualTo(TEST_URL);

            }
        }
    }

}