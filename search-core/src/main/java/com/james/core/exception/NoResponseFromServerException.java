package com.james.core.exception;

import com.james.core.error.ErrorCode;

public class NoResponseFromServerException extends BaseException {
    public NoResponseFromServerException() {
        super(ErrorCode.NO_RESPONSE_FROM_SERVER, ErrorCode.NO_RESPONSE_FROM_SERVER.getMessage());
    }
}
