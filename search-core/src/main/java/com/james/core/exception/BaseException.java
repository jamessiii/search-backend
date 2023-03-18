package com.james.core.exception;

import com.james.core.error.ErrorCode;
import lombok.Getter;
import org.springframework.web.server.ResponseStatusException;

public class BaseException extends ResponseStatusException {

    @Getter
    private final ErrorCode errorCode;

    @Getter
    private final String logMessage;

    public BaseException(ErrorCode errorCode, String logMessage) {
        super(errorCode.getHttpStatus(), errorCode.getMessage());
        this.errorCode = errorCode;
        this.logMessage = logMessage;
    }
}
