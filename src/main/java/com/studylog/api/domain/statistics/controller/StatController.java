package com.studylog.api.domain.statistics.controller;

import com.studylog.api.domain.statistics.dto.StatDto;
import com.studylog.api.domain.statistics.service.StatService;
import com.studylog.api.global.auth.CurrentMemberProvider;
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
    private final CurrentMemberProvider currentMemberProvider;

    @Operation(summary = "특정 날짜 과목별 통계", description = "날짜 선택 시 과목별 학습시간을 조회합니다.")
    @GetMapping("/daily")
    public ResponseEntity<StatDto> getDailySubjects(
            @RequestHeader("Authorization") String authorization,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        Long memberId = currentMemberProvider.getMemberId(authorization);
        return ResponseEntity.ok(statService.getDailySubjects(memberId, date));
    }

    @Operation(summary = "주간 통계 조회", description = "주간 총합 + 날짜별 + 과목별 통계를 조회합니다.")
    @GetMapping("/weekly")
    public ResponseEntity<StatDto> getWeekly(
            @RequestHeader("Authorization") String authorization,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate weekStart) {
        Long memberId = currentMemberProvider.getMemberId(authorization);
        return ResponseEntity.ok(statService.getWeekly(memberId, weekStart));
    }

    @Operation(summary = "월간 통계 조회", description = "월간 총합 + 날짜별 + 과목별 통계를 조회합니다.")
    @GetMapping("/monthly")
    public ResponseEntity<StatDto> getMonthly(
            @RequestHeader("Authorization") String authorization,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth month) {
        Long memberId = currentMemberProvider.getMemberId(authorization);
        return ResponseEntity.ok(statService.getMonthly(memberId, month));
    }

    @Operation(summary = "오늘 총 학습시간 조회", description = "오늘의 실시간 총 학습시간을 조회합니다.")
    @GetMapping("/today/total")
    public ResponseEntity<Long> getTodayStudyTime(
            @RequestHeader("Authorization") String authorization) {
        Long memberId = currentMemberProvider.getMemberId(authorization);
        return ResponseEntity.ok(statService.getTodayStudyTime(memberId));
    }

    @Operation(summary = "오늘 과목별 학습 통계", description = "오늘 과목별 학습시간을 조회합니다.")
    @GetMapping("/today")
    public ResponseEntity<StatDto> getTodayStats(
            @RequestHeader("Authorization") String authorization) {
        Long memberId = currentMemberProvider.getMemberId(authorization);
        return ResponseEntity.ok(statService.getTodayStats(memberId));
    }
}