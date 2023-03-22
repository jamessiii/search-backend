package com.james.core.exception;

import com.james.core.error.ErrorCode;

public class RequestPageIsTooLargeUsingNaverApiException extends BaseException {
    public RequestPageIsTooLargeUsingNaverApiException() {
        super(ErrorCode.PAGE_IS_TOO_LARGE, ErrorCode.PAGE_IS_TOO_LARGE.getMessage());
    }
}
