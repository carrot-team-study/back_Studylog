package com.studylog.api.domain.subject.controller;

import com.studylog.api.domain.subject.dto.SubjectRequestDto;
import com.studylog.api.domain.subject.dto.SubjectResponseDto;
import com.studylog.api.domain.subject.service.SubjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Subject", description = "과목 관리 API")
@RestController
@RequestMapping("/api/sbjects")
@RequiredArgsConstructor
public class SubjectController {
    private final SubjectService subjectService;

    @Operation(summary = "과목 등록", description = "새로운 과목을 등록합니다.")
    @PostMapping
    public ResponseEntity<SubjectResponseDto> createSubject(
            @RequestHeader("memberId") Long memberId,
            @Valid @RequestBody SubjectRequestDto requestDto) {
        SubjectResponseDto response = subjectService.createSubject(memberId, requestDto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "과목 목록 조회", description = "회원의 과목 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<SubjectResponseDto>> getSubjectList(
            @RequestHeader("memberId") Long memberId) {
        List<SubjectResponseDto> responses = subjectService.getSubjectList(memberId);
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "과목 수정", description = "과목 정보를 수정합니다.")
    @PutMapping("/{subjectId}")
    public ResponseEntity<SubjectResponseDto> updateSubject(
            @RequestHeader("memberId") Long memberId,
            @PathVariable Long subjectId,
            @Valid @RequestBody SubjectRequestDto requestDto) {
        SubjectResponseDto response = subjectService.updateSubject(memberId, subjectId, requestDto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "과목 삭제", description = "과목을 삭제합니다. (Soft Delete)")
    @DeleteMapping("/{subjectId}")
    public ResponseEntity<Void> deleteSubject(
            @RequestHeader("memberId") Long memberId,
            @PathVariable Long subjectId) {
        subjectService.deleteSubject(memberId, subjectId);
        return ResponseEntity.noContent().build();
    }
}
