package com.studylog.api.domain.timer.controller;

import com.studylog.api.domain.timer.dto.request.ManualTimerRequest;
import com.studylog.api.domain.timer.dto.request.TimerStartRequest;
import com.studylog.api.domain.timer.dto.response.StudyLogSummaryResponse;
import com.studylog.api.domain.timer.dto.response.TimerStatusResponse;
import com.studylog.api.domain.timer.dto.response.TimerStopResponse;
import com.studylog.api.domain.timer.entity.Timer;
import com.studylog.api.domain.timer.service.TimerService;
import com.studylog.api.global.common.code.SuccessCode;
import com.studylog.api.global.common.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/timer")
public class TimerController {
    // 의존 생성자 주입
    private final TimerService timerService;

    // 타이머 시작
    @PostMapping("/start")
    public ResponseEntity<SuccessResponse<Void>> start(@RequestParam Long memberId, @RequestParam Long subjectId) {
        // 서비스 호출
        timerService.start(memberId, subjectId);
        SuccessCode sc = SuccessCode.TIMER_START_SUCCESS;
        return ResponseEntity.status(sc.getHttpStatus()).body(SuccessResponse.success(sc));
    }

    // 타이머 일시정지
    @PostMapping("/pause")
    public ResponseEntity<SuccessResponse<Void>> pause(@RequestParam Long memberId, @RequestParam Long subjectId) {
        timerService.pause(memberId, subjectId);
        SuccessCode sc = SuccessCode.TIMER_PAUSE_SUCCESS;
        return ResponseEntity.status(sc.getHttpStatus()).body(SuccessResponse.success(sc));
    }

    // 타이머 재개
    @PostMapping("/resume")
    public ResponseEntity<SuccessResponse<Void>> resume(@RequestParam Long memberId, @RequestParam Long subjectId) {
        timerService.resume(memberId, subjectId);
        SuccessCode sc = SuccessCode.TIMER_RESUME_SUCCESS;
        return ResponseEntity.status(sc.getHttpStatus()).body(SuccessResponse.success(sc));
    }

    // 타이머 종료
    @PostMapping("/stop")
    public ResponseEntity<SuccessResponse<TimerStopResponse>> stop(@RequestParam Long memberId, @RequestParam Long subjectId) {
        TimerStopResponse res = timerService.stop(memberId, subjectId);
        SuccessCode sc = SuccessCode.TIMER_END_SUCCESS;
        return ResponseEntity.status(sc.getHttpStatus()).body(SuccessResponse.success(sc, res));
    }

    // 타이머 상태 조회
    @GetMapping("/status")
    public ResponseEntity<SuccessResponse<TimerStatusResponse>> status(@RequestParam Long memberId, @RequestParam Long subjectId) {
        TimerStatusResponse res = timerService.status(memberId, subjectId);
        SuccessCode sc = SuccessCode.TIMER_STATUS_SUCCESS;
        return ResponseEntity.status(sc.getHttpStatus()).body(SuccessResponse.success(sc, res));
    }

    // 해당 날짜에 대한 타이머 내역 조회
    @GetMapping("/records")
    public ResponseEntity<SuccessResponse<List<Timer>>> getRecords(@RequestParam Long memberId, @RequestParam String date) {
        LocalDate timerDate = LocalDate.parse(date);
        List<Timer> records = timerService.getRecords(memberId, timerDate);
        SuccessCode sc = SuccessCode.STUDY_LOG_LIST_SUCCESS;
        return ResponseEntity.status(sc.getHttpStatus()).body(SuccessResponse.success(sc, records));
    }

    // 수동 학습 기록
    @PostMapping("/records/manual")
    public ResponseEntity<SuccessResponse<TimerStopResponse>> manual(@RequestBody ManualTimerRequest request) {
        TimerStopResponse res = timerService.addManualRecord(request);
        SuccessCode sc = SuccessCode.MANUAL_STUDY_LOG_SUCCESS;
        return ResponseEntity.status(sc.getHttpStatus()).body(SuccessResponse.success(sc, res));
    }

    // 하루 총 학습 시간 요약
    @GetMapping("/records/summary")
    public ResponseEntity<SuccessResponse<StudyLogSummaryResponse>> summary(@RequestParam Long memberId, @RequestParam String date) {
        LocalDate targetDate = LocalDate.parse(date);
        StudyLogSummaryResponse res = timerService.getDailySummary(memberId, targetDate);
        SuccessCode sc = SuccessCode.STUDY_LOG_SUMMARY_SUCCESS;
        return ResponseEntity.status(sc.getHttpStatus()).body(SuccessResponse.success(sc, res));
    }
}
