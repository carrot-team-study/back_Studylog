package com.studylog.api.domain.todo.dto.response;

import com.studylog.api.domain.todo.entity.Todo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TodoResponse {
    private Long todoId;
    private Long subjectId;
    private String content;
    private LocalDate targetDate;
    private boolean completed;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static TodoResponse from(Todo todo) {
        return new TodoResponse(
                todo.getTodoId(),
                todo.getSubjectId(),
                todo.getContent(),
                todo.getTargetDate(),
                todo.isCompleted(),
                todo.getCreatedAt(),
                todo.getUpdatedAt()
        );
    }
}
