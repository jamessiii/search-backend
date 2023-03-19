package com.james.core.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러가 발생했습니다. 잠시 후 다시 시도해주세요."),
    NO_RESPONSE_FROM_SERVER(HttpStatus.INTERNAL_SERVER_ERROR, "서버가 응답하지 않습니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    NOT_FOUND_SEARCH(HttpStatus.NOT_FOUND, "Search 조회 결과가 없습니다."),
    NOT_FOUND_HISTORY(HttpStatus.NOT_FOUND, "History 조회 결과가 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
