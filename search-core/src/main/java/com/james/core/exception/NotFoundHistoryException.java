package com.james.core.exception;

import com.james.core.error.ErrorCode;

public class NotFoundHistoryException extends BaseException {

    public NotFoundHistoryException() {
        super(ErrorCode.NOT_FOUND_HISTORY, ErrorCode.NOT_FOUND_HISTORY.getMessage());
    }
}
