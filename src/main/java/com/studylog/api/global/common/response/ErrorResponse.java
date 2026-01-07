package com.studylog.api.global.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private final boolean success;
    private final String code;
    private final String message;
    private final List<ValidationError> errors; //상세에러내용
    private final LocalDateTime timestamp;
    private final String path; //에러가 난 요청 URL 경로

    @Getter
    @AllArgsConstructor
    public static class ValidationError {
        private final String field;
        private final String reason;
    }

    public static ErrorResponse error (String code, String message, List<ValidationError> errors, String path) {
        return ErrorResponse.builder()
                .success(false)
                .code(code)
                .message(message)
                .errors(errors)
                .timestamp(LocalDateTime.now())
                .path(path)
                .build();
    }

}
