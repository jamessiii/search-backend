package com.james.api.feign.decoder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.james.api.feign.dto.GetSearchNaverBlogResponseDto;
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
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class FeignNaverDecoderTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String TEST_LINK = "TEST_LINK";
    private static final String TEST_TITLE = "TEST_TITLE";
    private static final String TEST_DESCRIPTION = "TEST_DESCRIPTION";
    private static final String TEST_BLOGGER_NAME = "TEST_BLOGGER_NAME";
    private static final LocalDateTime TEST_DATETIME = LocalDateTime.of(2023,3,20,0,0,0);
    private static final String TEST_POST_DATE = "20230320";
    private static final Integer TEST_TOTAL_COUNT = 10;

    @InjectMocks
    private FeignNaverDecoder feignNaverDecoder;

    @Nested
    @DisplayName("FeignNaverDecoder decode 메소드는")
    class DescribeDecodeInFeignNaverDecoder {

        Response response;
        Map<String, Collection<String>> header = new LinkedHashMap<>();
        Request.Body body = Request.Body.create("");
        Request request = Request.create(Request.HttpMethod.GET, TEST_LINK, header, body, null);

        @Nested
        @DisplayName("서버에서 정상응답을 받으면")
        class ContextWithNormalResponse {

            @BeforeEach
            void setup() throws JsonProcessingException {

                Map<String, String> item = new LinkedHashMap<>();
                item.put("title", TEST_TITLE);
                item.put("description", TEST_DESCRIPTION);
                item.put("link", TEST_LINK);
                item.put("bloggername", TEST_BLOGGER_NAME);
                item.put("postdate", TEST_POST_DATE);

                List<Map<String, String>> itemList = new ArrayList<>();
                itemList.add(item);

                Map<String, Object> responseBody = new LinkedHashMap<>();
                responseBody.put("total", TEST_TOTAL_COUNT.toString());
                responseBody.put("items", itemList);

                String body = objectMapper.writeValueAsString(responseBody);
                response = Response.builder()
                        .status(HttpStatus.OK.value())
                        .body(body, StandardCharsets.UTF_8)
                        .request(request).build();
            }

            @Test
            @DisplayName("response body data 를 ResponseDto Type 으로 파싱하여 리턴한다.")
            void itReturnResponseBodyDataParsedResponseDtoType() throws IOException {

                Object responseDto = feignNaverDecoder.decode(response, GetSearchNaverBlogResponseDto.class);

                assertThat(responseDto.getClass()).isEqualTo(GetSearchNaverBlogResponseDto.class);
                GetSearchNaverBlogResponseDto response = (GetSearchNaverBlogResponseDto)responseDto;
                assertThat(response.getTotalCount()).isEqualTo(TEST_TOTAL_COUNT);
                assertThat(response.getDocumentList().get(0).getContents()).isEqualTo(TEST_DESCRIPTION);
                assertThat(response.getDocumentList().get(0).getBlogName()).isEqualTo(TEST_BLOGGER_NAME);
                assertThat(response.getDocumentList().get(0).getDateTime()).isEqualTo(TEST_DATETIME);
                assertThat(response.getDocumentList().get(0).getTitle()).isEqualTo(TEST_TITLE);
                assertThat(response.getDocumentList().get(0).getUrl()).isEqualTo(TEST_LINK);
            }
        }
    }

}