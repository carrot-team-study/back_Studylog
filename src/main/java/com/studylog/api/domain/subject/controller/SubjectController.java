package com.studylog.api.domain.subject.controller;

import com.studylog.api.domain.subject.dto.SubjectRequestDto;
import com.studylog.api.domain.subject.dto.SubjectResponseDto;
import com.studylog.api.domain.subject.service.SubjectService;
import com.studylog.api.global.auth.CurrentMemberProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Subject", description = "과목 관리 API")
@RestController
@RequestMapping("/api/subjects")
@RequiredArgsConstructor
public class SubjectController {
    private final SubjectService subjectService;
    private final CurrentMemberProvider currentMemberProvider;

    @Operation(summary = "과목 등록", description = "새로운 과목을 등록합니다.")
    @PostMapping
    public ResponseEntity<SubjectResponseDto> createSubject(
            @RequestHeader("Authorization") String authorization, // 변경
            @Valid @RequestBody SubjectRequestDto requestDto) {
        Long memberId = currentMemberProvider.getMemberId(authorization); // 추가
        SubjectResponseDto response = subjectService.createSubject(memberId, requestDto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "과목 목록 조회", description = "회원의 과목 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<SubjectResponseDto>> getSubjectList(
            @RequestHeader("Authorization") String authorization) {
        Long memberId = currentMemberProvider.getMemberId(authorization);
        List<SubjectResponseDto> responses = subjectService.getSubjectList(memberId);
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "과목 수정", description = "과목 정보를 수정합니다.")
    @PutMapping("/{subjectId}")
    public ResponseEntity<SubjectResponseDto> updateSubject(
            @RequestHeader("Authorization") String authorization,
            @PathVariable Long subjectId,
            @Valid @RequestBody SubjectRequestDto requestDto) {
        Long memberId = currentMemberProvider.getMemberId(authorization);
        SubjectResponseDto response = subjectService.updateSubject(memberId, subjectId, requestDto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "과목 삭제", description = "과목을 삭제합니다. (Soft Delete)")
    public ResponseEntity<Void> deleteSubject(
            @RequestHeader("Authorization") String authorization,
            @PathVariable Long subjectId) {
        Long memberId = currentMemberProvider.getMemberId(authorization);
        subjectService.deleteSubject(memberId, subjectId);
        return ResponseEntity.noContent().build();
    }
}
