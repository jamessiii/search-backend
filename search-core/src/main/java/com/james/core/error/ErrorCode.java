package com.james.core.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    NO_RESPONSE_FROM_SERVER(HttpStatus.INTERNAL_SERVER_ERROR, "서버가 응답하지 않습니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    NOT_FOUND_SEARCH(HttpStatus.NOT_FOUND, "Search 조회 결과가 없습니다."),
    PAGE_IS_TOO_LARGE(HttpStatus.BAD_REQUEST, "현재 카카오 API 서버가 불안정하여 네이버 API 서버를 이용중입니다. " +
            "네이버 검색 시 Page는 100을 초과할 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
