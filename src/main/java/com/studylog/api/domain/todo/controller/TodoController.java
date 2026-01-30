package com.studylog.api.domain.todo.controller;

import com.studylog.api.domain.todo.dto.request.TodoCreateRequest;
import com.studylog.api.domain.todo.dto.request.TodoUpdateRequest;
import com.studylog.api.domain.todo.dto.response.TodoResponse;
import com.studylog.api.domain.todo.service.TodoService;
import com.studylog.api.global.auth.CurrentMemberProvider;
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
    // 로그인한 회원 정보 제공
    private final CurrentMemberProvider currentMemberProvider;

    // 전체 조회
    @GetMapping
    public ResponseEntity<SuccessResponse<List<TodoResponse>>> list(@RequestHeader("Authorization") String authorization) {
        // 로그인 회원 ID 가져오기
        Long memberId = currentMemberProvider.getMemberId(authorization);
        List<TodoResponse> todos = todoService.getAll(memberId);
        SuccessCode sc = SuccessCode.TODO_LIST_SUCCESS;
        return ResponseEntity.status(sc.getHttpStatus()).body(SuccessResponse.success(sc, todos));
    }

    // 날짜별 조회
    @GetMapping("/date")
    public ResponseEntity<SuccessResponse<List<TodoResponse>>> listByDate(@RequestHeader("Authorization") String authorization, @RequestParam String date) {
        // 로그인 회원 ID 가져오기
        Long memberId = currentMemberProvider.getMemberId(authorization);
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
    public ResponseEntity<SuccessResponse<Void>> create(@RequestHeader("Authorization") String authorization, @RequestBody TodoCreateRequest request) {
        // 로그인 회원 ID 가져오기
        Long memberId = currentMemberProvider.getMemberId(authorization);
        todoService.create(memberId, request);
        SuccessCode sc = SuccessCode.TODO_CREATE_SUCCESS;
        return ResponseEntity.status(sc.getHttpStatus()).body(SuccessResponse.success(sc));
    }

    // 수정
    @PutMapping("/{todoId}")
    public ResponseEntity<SuccessResponse<Void>> update(@RequestHeader("Authorization") String authorization, @PathVariable Long todoId, @RequestBody TodoUpdateRequest request) {
        // 로그인 회원 ID 가져오기
        Long memberId = currentMemberProvider.getMemberId(authorization);

        // 본인 소유 여부 체크
        todoService.update(todoId, request, memberId);

        SuccessCode sc = SuccessCode.TODO_UPDATE_SUCCESS;
        return ResponseEntity.status(sc.getHttpStatus()).body(SuccessResponse.success(sc));
    }

    // 삭제
    @DeleteMapping("/{todoId}")
    public ResponseEntity<SuccessResponse<Void>> delete(@RequestHeader("Authorization") String authorization, @PathVariable Long todoId) {
        // 로그인 회원 ID 가져오기
        Long memberId = currentMemberProvider.getMemberId(authorization);

        // 본인 소유 여부 체크
        todoService.delete(todoId, memberId);

        SuccessCode sc = SuccessCode.TODO_DELETE_SUCCESS;
        return ResponseEntity.status(sc.getHttpStatus()).body(SuccessResponse.success(sc));
    }

    // 완료, 취소 상태
    @PatchMapping("/{todoId}/complete")
    public ResponseEntity<SuccessResponse<Void>> complete(@RequestHeader("Authorization") String authorization, @PathVariable Long todoId, @RequestParam boolean completed) {
        // 로그인 회원 ID 가져오기
        Long memberId = currentMemberProvider.getMemberId(authorization);

        // 본인 소유 여부 체크
        todoService.updateComplete(todoId, completed, memberId);

        SuccessCode sc = SuccessCode.TODO_STATUS_UPDATE_SUCCESS;
        return ResponseEntity.status(sc.getHttpStatus()).body(SuccessResponse.success(sc));
    }
}
