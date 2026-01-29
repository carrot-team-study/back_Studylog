package com.studylog.api.domain.plan.controller;

import com.studylog.api.domain.plan.dto.PlanRequestDto;
import com.studylog.api.domain.plan.dto.PlanResponseDto;
import com.studylog.api.domain.plan.entity.Plan;
import com.studylog.api.domain.plan.repository.PlanRepository;
import com.studylog.api.domain.plan.service.PlanService;
import lombok.RequiredArgsConstructor;
import org.aspectj.bridge.MessageUtil;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/plans")
@RequiredArgsConstructor
public class PlanController {
    private final PlanService planService;

    @PostMapping
    public ResponseEntity<PlanResponseDto> createPlan(@RequestBody PlanRequestDto requestDto) {
        Long loginMemberId = 1L; //수정필요
        PlanResponseDto response = planService.createPlan(requestDto, loginMemberId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<PlanResponseDto>> getPlansByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        Long loginMemberId = 1L; //수정필요
        return ResponseEntity.ok(planService.getPlansByDate(loginMemberId, date));
    }

    @GetMapping("/{planId}")
    public ResponseEntity<PlanResponseDto> getPlan(@PathVariable Long planId) {
        return ResponseEntity.ok(planService.getPlanById(planId));
    }

    @PutMapping("/{planId}")
    public ResponseEntity<PlanResponseDto> updatePlan(
            @PathVariable Long planId,
            @RequestBody PlanRequestDto requestDto) {
        return ResponseEntity.ok(planService.updatePlan(planId, requestDto));
    }

    @DeleteMapping("/{planId}")
    public ResponseEntity<Void> deletePlan(@PathVariable Long planId) {
        planService.deletePlan(planId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{planId}/completion")
    public ResponseEntity<PlanResponseDto> togglePlan(@PathVariable Long planId) {
        PlanResponseDto response = planService.togglePlanCompletion(planId);
        return ResponseEntity.ok(response);
    }
}
