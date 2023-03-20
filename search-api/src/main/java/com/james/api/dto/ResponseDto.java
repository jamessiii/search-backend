package com.james.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.james.core.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDto<T> {

    private T data;

    private Error error;

    public static <T> ResponseDto<T> success(T data) {
        return new ResponseDto<>(data, null);
    }

    public static ResponseDto<Void> error(Error error) {
        return new ResponseDto<>(null, error);
    }

    public record Error(ErrorCode code, String message) {

    }
}
