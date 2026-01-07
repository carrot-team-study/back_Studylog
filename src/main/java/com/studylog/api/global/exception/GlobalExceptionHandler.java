package com.studylog.api.global.exception;

import com.studylog.api.global.common.code.ErrorCode;
import com.studylog.api.global.common.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusiness(BusinessException e, HttpServletRequest req) {
        ErrorCode ec = e.getErrorCode();
        return ResponseEntity
                .status(ec.getHttpStatus())
                .body(ErrorResponse.error(ec.getCode(), ec.getMessage(), null, req.getRequestURI()));
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException e, HttpServletRequest req) {
        List<ErrorResponse.ValidationError> errors = e.getBindingResult().getFieldErrors().stream()
                .map(this::toValidationError)
                .toList();

        ErrorCode ec = ErrorCode.VALIDATION_FAIL;
        return ResponseEntity
                .status(ec.getHttpStatus())
                .body(ErrorResponse.error(ec.getCode(), ec.getMessage(), errors, req.getRequestURI()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnknown(Exception e, HttpServletRequest req) {
        ErrorCode ec = ErrorCode.INTERNAL_SERVER_ERROR;
        return ResponseEntity
                .status(ec.getHttpStatus())
                .body(ErrorResponse.error(ec.getCode(), ec.getMessage(), null, req.getRequestURI()));
    }

    private ErrorResponse.ValidationError toValidationError(FieldError fe) {
        return new ErrorResponse.ValidationError(
                fe.getField(),
                fe.getDefaultMessage() == null ? "Invalid" : fe.getDefaultMessage()
        );
    }

}
