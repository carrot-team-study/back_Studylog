package com.studylog.api.domain.reflection.dto.response;

import com.studylog.api.domain.reflection.entity.Reflection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ReflectionResponseDto {

    private Long reflectionId;
    private String content;
    private LocalDate createdAt;

    public static ReflectionResponseDto from(Reflection reflection) {
        return new ReflectionResponseDto(
                reflection.getReflectionId(),
                reflection.getContent(),
                reflection.getCreatedAt()
        );
    }
}