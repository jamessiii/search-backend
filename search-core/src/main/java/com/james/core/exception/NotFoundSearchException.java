package com.james.core.exception;

import com.james.core.error.ErrorCode;

public class NotFoundSearchException extends BaseException {
    public NotFoundSearchException() {
        super(ErrorCode.NOT_FOUND_SEARCH, ErrorCode.NOT_FOUND_SEARCH.getMessage());
    }
}
