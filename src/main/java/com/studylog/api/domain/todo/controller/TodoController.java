package com.studylog.api.domain.todo.controller;

import com.studylog.api.domain.todo.dto.request.TodoCreateRequest;
import com.studylog.api.domain.todo.dto.request.TodoUpdateRequest;
import com.studylog.api.domain.todo.dto.response.TodoResponse;
import com.studylog.api.domain.todo.service.TodoService;
import com.studylog.api.global.common.code.ErrorCode;
import com.studylog.api.global.common.code.SuccessCode;
import com.studylog.api.global.common.response.SuccessResponse;
import com.studylog.api.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/todos")
public class TodoController {

    private final TodoService todoService;

    // 전체 조회
    @GetMapping
    public ResponseEntity<SuccessResponse<List<TodoResponse>>> list(@RequestHeader("memberId") Long memberId) {
        List<TodoResponse> todos = todoService.getAll(memberId);
        SuccessCode sc = SuccessCode.TODO_LIST_SUCCESS;
        return ResponseEntity.status(sc.getHttpStatus()).body(SuccessResponse.success(sc, todos));
    }

    // 날짜별 조회
    @GetMapping("/date")
    public ResponseEntity<SuccessResponse<List<TodoResponse>>> listByDate(@RequestHeader("memberId") Long memberId, @RequestParam String date) {
        LocalDate targetDate;
        try {
            targetDate = LocalDate.parse(date);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.INVALID_TODO_DATE);
        }

        List<TodoResponse> todos = todoService.getByDate(memberId, targetDate);
        SuccessCode sc = SuccessCode.TODO_LIST_BY_DATE_SUCCESS;
        return ResponseEntity.status(sc.getHttpStatus()).body(SuccessResponse.success(sc, todos));
    }

    // 작성
    @PostMapping
    public ResponseEntity<SuccessResponse<Void>> create(@RequestHeader("memberId") Long memberId, @RequestBody TodoCreateRequest request) {
        todoService.create(memberId, request);
        SuccessCode sc = SuccessCode.TODO_CREATE_SUCCESS;
        return ResponseEntity.status(sc.getHttpStatus()).body(SuccessResponse.success(sc));
    }

    // 수정
    @PutMapping("/{todoId}")
    public ResponseEntity<SuccessResponse<Void>> update(@PathVariable Long todoId, @RequestBody TodoUpdateRequest request) {
        todoService.update(todoId, request);
        SuccessCode sc = SuccessCode.TODO_UPDATE_SUCCESS;
        return ResponseEntity.status(sc.getHttpStatus()).body(SuccessResponse.success(sc));
    }

    // 삭제
    @DeleteMapping("/{todoId}")
    public ResponseEntity<SuccessResponse<Void>> delete(@PathVariable Long todoId) {
        todoService.delete(todoId);
        SuccessCode sc = SuccessCode.TODO_DELETE_SUCCESS;
        return ResponseEntity.status(sc.getHttpStatus()).body(SuccessResponse.success(sc));
    }

    // 완료, 취소 상태
    @PatchMapping("/{todoId}/complete")
    public ResponseEntity<SuccessResponse<Void>> complete(@PathVariable Long todoId, @RequestParam boolean completed) {
        todoService.updateComplete(todoId, completed);
        SuccessCode sc = SuccessCode.TODO_STATUS_UPDATE_SUCCESS;
        return ResponseEntity.status(sc.getHttpStatus()).body(SuccessResponse.success(sc));
    }
}
