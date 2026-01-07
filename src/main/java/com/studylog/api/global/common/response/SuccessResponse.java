package com.studylog.api.global.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.studylog.api.global.common.code.SuccessCode;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SuccessResponse<T> {

    private final boolean success;
    private final String code;
    private final String message;
    private final T data;
    private final LocalDateTime timestamp;

    //성공응답, 데이터 있음
    public static <T> SuccessResponse<T> success(SuccessCode sc, T data) {
        return SuccessResponse.<T>builder()
                .success(true)
                .code(sc.getCode())
                .message(sc.getMessage())
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    //데이터 없음
    public static SuccessResponse<Void> success(SuccessCode sc) {
        return SuccessResponse.<Void>builder()
                .success(true)
                .code(sc.getCode())
                .message(sc.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }


}
