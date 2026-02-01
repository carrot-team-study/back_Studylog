package com.studylog.api.domain.todo.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class TodoUpdateRequest {
    private String content;
    private LocalDate targetDate;
}
