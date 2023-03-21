package com.james.core.exception;

import com.james.core.error.ErrorCode;

public class NaverApiPageIsTooLargeException extends BaseException {
    public NaverApiPageIsTooLargeException() {
        super(ErrorCode.PAGE_IS_TOO_LARGE, ErrorCode.PAGE_IS_TOO_LARGE.getMessage());
    }
}
