package com.sontaypham.storemvc.exception;

import com.sontaypham.storemvc.dto.response.ApiResponse;
import com.sontaypham.storemvc.enums.ErrorCode;
import jakarta.validation.ConstraintViolation;
import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

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

  @ExceptionHandler(value = MethodArgumentNotValidException.class)
  public ResponseEntity<ApiResponse<Map<String, String>>> handleValidation(
      MethodArgumentNotValidException ex) {
    ErrorCode errorCode = ErrorCode.UNCATEGORIZED;
    for (var err : ex.getBindingResult().getAllErrors()) {
      String key = err.getDefaultMessage();
      try {
        errorCode = ErrorCode.valueOf(key);
        break;
      } catch (IllegalArgumentException e) {
        log.warn("Unknown validation key: {}", key);
      }
    }

    Map<String, Object> attributes = null;
    try {
      var violation =
          ex.getBindingResult().getAllErrors().getFirst().unwrap(ConstraintViolation.class);
      attributes = violation.getConstraintDescriptor().getAttributes();
    } catch (Exception e) {
      log.error(e.getMessage());
    }
    return ResponseEntity.status(errorCode.getHttpStatus())
        .body(
            ApiResponse.<Map<String, String>>builder()
                .status(errorCode.getCode())
                .message(
                    Objects.nonNull(attributes)
                        ? mapAttributes(errorCode.getMessage(), attributes)
                        : errorCode.getMessage())
                .build());
  }

  private String mapAttributes(String message, Map<String, Object> attributes) {
    for (Map.Entry<String, Object> entry : attributes.entrySet()) {
      message = message.replace("{" + entry.getKey() + "}", String.valueOf(entry.getValue()));
    }
    return message;
  }
}
