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
     * 일간 통계 조회
     */
    public List<StatDto> getDailyStats(Long memberId, LocalDate startDate, LocalDate endDate) {
        List<StatDto> stats = statMapper.getStatsList(memberId, startDate, endDate).stream()
                .map(stat -> StatDto.builder()
                        .periodType("DAILY")
                        .startDate(stat.getStartDate())
                        .totalStudyTime(stat.getTotalStudyTime())
                        .subjects(statMapper.getSubjectStats(memberId, stat.getStartDate(), stat.getStartDate()))
                        .build())
                .collect(Collectors.toList());

        // 오늘이 조회 기간 안에 포함되면 실시간 데이터 추가
        if (!endDate.isBefore(LocalDate.now())) {
            long todayStudyTime = statMapper.getTodayStudyTime(memberId, LocalDate.now());
            stats.add(StatDto.builder()
                    .periodType("DAILY")
                    .startDate(LocalDate.now())
                    .totalStudyTime(todayStudyTime)
                    .subjects(statMapper.getSubjectStats(memberId, LocalDate.now(), LocalDate.now()))
                    .build());
        }

        return stats;
    }

    /**
     * 주간 통계 조회 (총합 + 날짜별 + 과목별)
     */
    public StatDto getWeekly(Long memberId, LocalDate weekStart) {
        LocalDate monday = weekStart.with(java.time.temporal.TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
        LocalDate sunday = monday.plusDays(6);

        List<StatDto> dailyStats = getDailyStats(memberId, monday, sunday);

        long weeklyTotal = dailyStats.stream()
                .mapToLong(StatDto::getTotalStudyTime)
                .sum();

        return StatDto.builder()
                .periodType("WEEKLY")
                .startDate(monday)
                .totalStudyTime(weeklyTotal)
                .subjects(statMapper.getSubjectStats(memberId, monday, sunday))
                .dailyStats(dailyStats)
                .build();
    }

    /**
     * 월간 통계 조회 (총합 + 날짜별 + 과목별)
     */
    public StatDto getMonthly(Long memberId, YearMonth month) {
        LocalDate startDate = month.atDay(1);
        LocalDate endDate = month.atEndOfMonth();  // ← minusDays(1) 제거

        List<StatDto> dailyStats = getDailyStats(memberId, startDate, endDate);

        long monthlyTotal = dailyStats.stream()
                .mapToLong(StatDto::getTotalStudyTime)
                .sum();

        return StatDto.builder()
                .periodType("MONTHLY")
                .startDate(startDate)
                .totalStudyTime(monthlyTotal)
                .subjects(statMapper.getSubjectStats(memberId, startDate, endDate))
                .dailyStats(dailyStats)
                .build();
    }

    /**
     * 오늘 총 학습 시간 (실시간)
     */
    public Long getTodayStudyTime(Long memberId) {
        return statMapper.getTodayStudyTime(memberId, LocalDate.now());
    }

    /**
     * 오늘 총 학습 시간 + 과목별 학습 시간
     */
    public StatDto getTodayStats(Long memberId) {
        LocalDate today = LocalDate.now();

        return StatDto.builder()
                .periodType("DAILY")
                .startDate(today)
                .totalStudyTime(statMapper.getTodayStudyTime(memberId, today))
                .subjects(statMapper.getSubjectStats(memberId, today, today))
                .build();
    }
}