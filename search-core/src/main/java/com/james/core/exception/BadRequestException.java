package com.james.core.exception;

import com.james.core.error.ErrorCode;

public class BadRequestException extends BaseException{

    public BadRequestException() {
        super(ErrorCode.BAD_REQUEST, ErrorCode.BAD_REQUEST.getMessage());
    }

    public BadRequestException(String message) {
        super(ErrorCode.BAD_REQUEST, message);
    }
}
