package com.studylog.api.domain.statistics.controller;

import com.studylog.api.domain.statistics.dto.StatDto;
import com.studylog.api.domain.statistics.service.StatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Tag(name = "Statistics", description = "통계 API")
@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
@Slf4j
public class StatController {

    private final StatService statService;

    @Operation(summary = "일간 통계 조회", description = "특정 기간의 일별 학습 통계를 조회합니다.")
    @GetMapping("/daily")
    public ResponseEntity<List<StatDto>> getDailyStats(
            @RequestHeader("memberId") Long memberId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(statService.getDailyStats(memberId, startDate, endDate));
    }

    @Operation(summary = "주간 통계 조회", description = "특정 주의 일별 학습 통계를 조회합니다 (7일)")
    @GetMapping("/weekly")
    public ResponseEntity<List<StatDto>> getWeeklyStats(
            @RequestHeader("memberId") Long memberId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate weekStart) {
        return ResponseEntity.ok(statService.getWeeklyStats(memberId, weekStart));
    }

    @Operation(summary = "주간 합계 조회", description = "특정 주의 총 학습시간을 조회합니다.")
    @GetMapping("/weekly/summary")
    public ResponseEntity<StatDto> getWeeklySummary(
            @RequestHeader("memberId") Long memberId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate weekStart) {
        return ResponseEntity.ok(statService.getWeeklySummary(memberId, weekStart));
    }

    @Operation(summary = "월간 통계 조회", description = "특정 월의 일별 학습 통계를 조회합니다.")
    @GetMapping("/monthly")
    public ResponseEntity<List<StatDto>> getMonthlyStats(
            @RequestHeader("memberId") Long memberId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth month) {
        return ResponseEntity.ok(statService.getMonthlyStats(memberId, month));
    }

    @Operation(summary = "월간 합계 조회", description = "특정 월의 총 학습시간을 조회합니다.")
    @GetMapping("/monthly/summary")
    public ResponseEntity<StatDto> getMonthlySummary(
            @RequestHeader("memberId") Long memberId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth month) {
        return ResponseEntity.ok(statService.getMonthlySummary(memberId, month));
    }

    @Operation(summary = "과목별 통계 조회", description = "특정 기간의 과목별 학습 통계를 조회합니다.")
    @GetMapping("/subjects")
    public ResponseEntity<List<StatDto>> getSubjectStats(
            @RequestHeader("memberId") Long memberId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(statService.getSubjectStats(memberId, startDate, endDate));
    }

    @Operation(summary = "오늘 학습시간 조회", description = "오늘의 실시간 학습시간을 조회합니다.")
    @GetMapping("/today")
    public ResponseEntity<Long> getTodayStudyTime(
            @RequestHeader("memberId") Long memberId) {
        return ResponseEntity.ok(statService.getTodayStudyTime(memberId));
    }
}