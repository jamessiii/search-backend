package com.james.api.configuration;

import com.james.api.dto.ResponseDto;
import com.james.core.error.ErrorCode;
import com.james.core.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Optional;

@Slf4j
@ControllerAdvice
public class ControllerExceptionAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<Object> handleBaseException(BaseException baseException) {
        return errorResponse(baseException.getStatus(), baseException.getErrorCode(),
                baseException.getReason(), baseException.getLogMessage());
    }

    private ResponseEntity<Object> errorResponse(HttpStatus status, ErrorCode errorCode, String message, String logMessage) {
        String responseMessage = logMessage.isEmpty() ? message : logMessage;
        return ResponseEntity.status(status)
                .body(ResponseDto.error(new ResponseDto.Error(errorCode, Optional.ofNullable(responseMessage).orElse(""))));
    }
}
