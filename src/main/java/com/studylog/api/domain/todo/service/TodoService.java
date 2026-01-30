package com.studylog.api.domain.todo.service;

import com.studylog.api.domain.todo.dto.request.TodoCreateRequest;
import com.studylog.api.domain.todo.dto.request.TodoUpdateRequest;
import com.studylog.api.domain.todo.dto.response.TodoResponse;
import com.studylog.api.domain.todo.entity.Todo;
import com.studylog.api.domain.todo.repository.TodoRepository;
import com.studylog.api.global.common.code.ErrorCode;
import com.studylog.api.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TodoService {

    private final TodoRepository todoRepository;

    // 전체 TodoList 조회
    @Transactional(readOnly = true)
    public List<TodoResponse> getAll(Long memberId) {
        return todoRepository.findAllByMemberId(memberId)
                .stream()
                .map(TodoResponse::from)
                .toList();
    }

    // 해당 날짜에 대한 TodoList 조회
    @Transactional(readOnly = true)
    public List<TodoResponse> getByDate(Long memberId, LocalDate targetDate) {
        return todoRepository.findAllByMemberIdAndTargetDate(memberId, targetDate)
                .stream()
                .map(TodoResponse::from)
                .toList();
    }

    // TodoList 작성
    public void create(Long memberId, TodoCreateRequest request) {
        if (request.getContent() == null || request.getContent().isBlank()) {
            throw new BusinessException(ErrorCode.INVALID_TODO_CONTENT);
        }

        Todo todo = Todo.builder()
                .memberId(memberId)
                .subjectId(request.getSubjectId())
                .content(request.getContent())
                .targetDate(request.getTargetDate())
                .build();

        todoRepository.save(todo);
    }

    // TodoList 수정
    public void update(Long todoId, TodoUpdateRequest request, Long memberId) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new BusinessException(ErrorCode.TODO_NOT_FOUND));

        // 본인 소유인지 체크
        if (!todo.getMemberId().equals(memberId)) {
            // 본인만 수정 가능
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }

        todo.update(request.getContent(), request.getTargetDate());
    }

    // TodoList 완료 상태
    public void updateComplete(Long todoId, boolean completed, Long memberId) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new BusinessException(ErrorCode.TODO_NOT_FOUND));

        // 본인 소유인지 체크
        if (!todo.getMemberId().equals(memberId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN); // 본인만 완료/취소 가능
        }

        todo.complete(completed);
    }

    // TodoList 삭제
    public void delete(Long todoId, Long memberId) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new BusinessException(ErrorCode.TODO_NOT_FOUND));

        // 본인 소유인지 체크
        if (!todo.getMemberId().equals(memberId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN); // 본인만 삭제 가능
        }

        todoRepository.delete(todo);
    }
}
