package com.studylog.api.domain.statistics.service;

import com.studylog.api.domain.statistics.dto.StatDto;
import com.studylog.api.mapper.statistics.StatMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class StatService {

    private final StatMapper statMapper;

    /**
     * 일간 통계 조회 (여러 날)
     */
    public List<StatDto> getDailyStats(Long memberId, LocalDate startDate, LocalDate endDate) {
        return statMapper.getStatsList(memberId, startDate, endDate).stream()
                .map(stat -> StatDto.builder()
                        .periodType("DAILY")
                        .startDate(stat.getStartDate())
                        .totalStudyTime(stat.getTotalStudyTime())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * 주간 통계 조회 (7일치 DAILY 데이터)
     */
    public List<StatDto> getWeeklyStats(Long memberId, LocalDate weekStart) {
        LocalDate weekEnd = weekStart.plusDays(6);
        return getDailyStats(memberId, weekStart, weekEnd);
    }

    /**
     * 주간 합계 (1개의 합산된 데이터)
     */
    public StatDto getWeeklySummary(Long memberId, LocalDate weekStart) {
        LocalDate weekEnd = weekStart.plusDays(6);

        List<StatDto> dailyStats = statMapper.getStatsList(memberId, weekStart, weekEnd);

        long weeklyTotal = dailyStats.stream()
                .mapToLong(StatDto::getTotalStudyTime)
                .sum();

        return StatDto.builder()
                .periodType("WEEKLY")
                .startDate(weekStart)
                .totalStudyTime(weeklyTotal)
                .build();
    }

    /**
     * 월간 통계 조회 (해당 월의 DAILY 데이터)
     */
    public List<StatDto> getMonthlyStats(Long memberId, YearMonth month) {
        LocalDate startDate = month.atDay(1);
        LocalDate endDate = month.atEndOfMonth();

        // 이번 달이면 오늘까지만
        if (month.equals(YearMonth.now())) {
            endDate = LocalDate.now().minusDays(1); // 어제까지 (오늘은 아직 집계 안됨)
        }

        return getDailyStats(memberId, startDate, endDate);
    }

    /**
     * 월간 합계 (1개의 합산된 데이터)
     */
    public StatDto getMonthlySummary(Long memberId, YearMonth month) {
        LocalDate startDate = month.atDay(1);
        LocalDate endDate = month.atEndOfMonth();

        if (month.equals(YearMonth.now())) {
            endDate = LocalDate.now().minusDays(1);
        }

        List<StatDto> dailyStats = statMapper.getStatsList(memberId, startDate, endDate);

        long monthlyTotal = dailyStats.stream()
                .mapToLong(StatDto::getTotalStudyTime)
                .sum();

        return StatDto.builder()
                .periodType("MONTHLY")
                .startDate(startDate)
                .totalStudyTime(monthlyTotal)
                .build();
    }

    /**
     * 과목별 통계 조회 (기간별)
     */
    public List<StatDto> getSubjectStats(Long memberId, LocalDate startDate, LocalDate endDate) {
        return statMapper.getSubjectStats(memberId, startDate, endDate);
    }

    /**
     * 오늘 학습 시간 (실시간)
     */
    public Long getTodayStudyTime(Long memberId) {
        return statMapper.getTodayStudyTime(memberId, LocalDate.now());
    }
}