package com.sontaypham.storemvc.exception;

import com.sontaypham.storemvc.dto.response.ApiResponse;
import com.sontaypham.storemvc.enums.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<ApiResponse<Void>> handlingRuntimeException() {
        return ResponseEntity.badRequest()
                             .body(
                                     ApiResponse.<Void>builder()
                                                .status(ErrorCode.UNCATEGORIZED.getCode())
                                                .message(ErrorCode.UNCATEGORIZED.getMessage())
                                                .build());
    }

    @ExceptionHandler(value = ApiException.class)
    public ResponseEntity<ApiResponse<Void>> handlingApiException(ApiException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity.status(errorCode.getHttpStatus())
                             .body(
                                     ApiResponse.<Void>builder()
                                                .status(errorCode.getCode())
                                                .message(errorCode.getMessage())
                                                .build());
    }
    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handlingAccessDeniedException() {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
        return ResponseEntity.status(errorCode.getHttpStatus())
                             .body(
                                     ApiResponse.<Void>builder()
                                                .status(errorCode.getCode())
                                                .message(errorCode.getMessage())
                                                .build());
    }

    private String mapAttributes(String message, Map<String, Object> attributes) {
        for (Map.Entry<String, Object> entry : attributes.entrySet()) {
            message = message.replace("{" + entry.getKey() + "}", String.valueOf(entry.getValue()));
        }
        return message;
    }
}
