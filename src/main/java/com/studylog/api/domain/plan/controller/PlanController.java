package com.studylog.api.domain.plan.controller;

import com.studylog.api.domain.plan.dto.PlanRequestDto;
import com.studylog.api.domain.plan.dto.PlanResponseDto;
import com.studylog.api.domain.plan.service.PlanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Plan", description = "계획 관리 API")
@RestController
@RequestMapping("/api/plans")
@RequiredArgsConstructor
public class PlanController {

    private final PlanService planService;

    @Operation(summary = "계획 등록", description = "새로운 계획을 등록합니다.")
    @PostMapping
    public ResponseEntity<PlanResponseDto> createPlan(
            @RequestHeader("memberId") Long memberId,
            @RequestBody PlanRequestDto requestDto) {
        PlanResponseDto response = planService.createPlan(requestDto, memberId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "날짜별 계획 목록 조회", description = "특정 날짜의 계획 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<PlanResponseDto>> getPlansByDate(
            @RequestHeader("memberId") Long memberId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(planService.getPlansByDate(memberId, date));
    }

    @Operation(summary = "계획 상세 조회", description = "계획 ID로 상세 정보를 조회합니다.")
    @GetMapping("/{planId}")
    public ResponseEntity<PlanResponseDto> getPlan(
            @RequestHeader("memberId") Long memberId,
            @PathVariable Long planId) {
        return ResponseEntity.ok(planService.getPlanById(memberId, planId));
    }

    @Operation(summary = "계획 수정", description = "계획 정보를 수정합니다.")
    @PutMapping("/{planId}")
    public ResponseEntity<PlanResponseDto> updatePlan(
            @RequestHeader("memberId") Long memberId,
            @PathVariable Long planId,
            @RequestBody PlanRequestDto requestDto) {
        return ResponseEntity.ok(planService.updatePlan(memberId, planId, requestDto));
    }

    @Operation(summary = "계획 삭제", description = "계획을 삭제합니다.")
    @DeleteMapping("/{planId}")
    public ResponseEntity<Void> deletePlan(
            @RequestHeader("memberId") Long memberId,
            @PathVariable Long planId) {
        planService.deletePlan(memberId, planId);
        return ResponseEntity.noContent().build();
    }
}
